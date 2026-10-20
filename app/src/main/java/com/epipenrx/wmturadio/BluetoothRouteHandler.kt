package com.epipenrx.wmturadio

import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

/**
 * Hands playback back and forth between headphones and a car's Bluetooth
 * head unit: when the active output disconnects (headphones -> car, or
 * car -> headphones), ACTION_AUDIO_BECOMING_NOISY fires and we pause; if a
 * Bluetooth A2DP device connects again within [Constants.BT_RESUME_WINDOW_MS],
 * we resume there automatically.
 *
 * This is the same route-change pause/resume shape as audiobookshelf-app's
 * BT auto-resume feature (PR #1914) -- a device mid-trip commonly bounces
 * headphones <-> car several times, and the original ABS bug was losing
 * playback state across the "becoming noisy" pause with no auto-resume on
 * the next connect.
 */
class BluetoothRouteHandler(
    private val context: Context,
    private val player: ExoPlayer
) {
    @Volatile
    private var pausedDueToRouteChange = false

    @Volatile
    private var pauseTimestampMs = 0L

    private var a2dpProxy: BluetoothProfile? = null
    private var registered = false

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter
    }

    private val a2dpServiceListener = object : BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            a2dpProxy = proxy
        }

        override fun onServiceDisconnected(profile: Int) {
            a2dpProxy = null
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            when (intent.action) {
                AudioManager.ACTION_AUDIO_BECOMING_NOISY -> onRouteLost()
                BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1)
                    if (state == BluetoothProfile.STATE_CONNECTED) onDeviceConnected()
                }
            }
        }
    }

    fun start() {
        if (registered) return
        val filter = IntentFilter().apply {
            addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
            addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            context.registerReceiver(receiver, filter)
        }
        if (hasBluetoothConnectPermission()) {
            bluetoothAdapter?.getProfileProxy(context, a2dpServiceListener, BluetoothProfile.A2DP)
        }
        registered = true
    }

    private fun hasBluetoothConnectPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return true
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun stop() {
        if (!registered) return
        context.unregisterReceiver(receiver)
        a2dpProxy?.let { bluetoothAdapter?.closeProfileProxy(BluetoothProfile.A2DP, it) }
        a2dpProxy = null
        registered = false
    }

    private fun onRouteLost() {
        if (player.isPlaying) {
            player.pause()
            pausedDueToRouteChange = true
            pauseTimestampMs = System.currentTimeMillis()
        }
    }

    private fun onDeviceConnected() {
        if (!pausedDueToRouteChange) return
        val withinWindow = System.currentTimeMillis() - pauseTimestampMs <= Constants.BT_RESUME_WINDOW_MS
        pausedDueToRouteChange = false
        if (withinWindow && player.playbackState != Player.STATE_IDLE) {
            player.play()
        }
    }
}
