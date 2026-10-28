package com.epipenrx.wmturadio

import android.Manifest
import android.app.AlarmManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.epipenrx.wmturadio.databinding.ActivityMainBinding
import com.google.common.util.concurrent.MoreExecutors

class MainActivity : AppCompatActivity(), PlaybackHost {

    private lateinit var binding: ActivityMainBinding
    private var controller: MediaController? = null
    private val hostListeners = mutableSetOf<Player.Listener>()
    private val readyCallbacks = mutableListOf<(MediaController) -> Unit>()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, PlayFragment())
            }
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_play -> PlayFragment()
                R.id.nav_options -> OptionsFragment()
                R.id.nav_about -> AboutFragment()
                else -> return@setOnItemSelectedListener false
            }
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, fragment)
            }
            true
        }

        requestRuntimePermissionsIfNeeded()
    }

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val future = MediaController.Builder(this, sessionToken).buildAsync()
        future.addListener(
            {
                val c = future.get()
                controller = c
                hostListeners.forEach { c.addListener(it) }
                val pending = readyCallbacks.toList()
                readyCallbacks.clear()
                pending.forEach { it(c) }
            },
            MoreExecutors.directExecutor()
        )
    }

    override fun onStop() {
        super.onStop()
        controller?.let { c -> hostListeners.forEach { c.removeListener(it) } }
        controller?.release()
        controller = null
    }

    override fun currentController(): MediaController? = controller

    override fun addPlaybackListener(listener: Player.Listener) {
        hostListeners.add(listener)
        controller?.addListener(listener)
    }

    override fun removePlaybackListener(listener: Player.Listener) {
        hostListeners.remove(listener)
        controller?.removeListener(listener)
    }

    override fun onControllerReady(callback: (MediaController) -> Unit) {
        val c = controller
        if (c != null) callback(c) else readyCallbacks.add(callback)
    }

    override fun playRespectingPreference() {
        when (Prefs.resumeMode(this)) {
            ResumeMode.CONTINUE -> controller?.play()
            ResumeMode.RELOAD -> reloadStream {}
        }
    }

    override fun reloadStream(onSettled: () -> Unit) {
        val c = controller
        if (c == null) {
            onSettled()
            return
        }
        val wasPlaying = c.isPlaying
        var settled = false

        lateinit var listener: Player.Listener
        val timeoutRunnable = Runnable {
            if (!settled) {
                settled = true
                c.removeListener(listener)
                onSettled()
            }
        }
        listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY && !settled) {
                    settled = true
                    handler.removeCallbacks(timeoutRunnable)
                    c.removeListener(listener)
                    onSettled()
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                if (!settled) {
                    settled = true
                    handler.removeCallbacks(timeoutRunnable)
                    c.removeListener(listener)
                    onSettled()
                }
            }
        }
        c.addListener(listener)
        handler.postDelayed(timeoutRunnable, RELOAD_TIMEOUT_MS)

        c.stop()
        c.prepare()
        if (wasPlaying) c.play()
    }

    private fun requestRuntimePermissionsIfNeeded() {
        val permissionsToRequest = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest += Manifest.permission.POST_NOTIFICATIONS
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest += Manifest.permission.BLUETOOTH_CONNECT
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        }

        maybeRequestExactAlarmPermission()
    }

    private fun maybeRequestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
        val alarmManager = getSystemService(AlarmManager::class.java)
        if (alarmManager?.canScheduleExactAlarms() == false) {
            startActivity(Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private const val RELOAD_TIMEOUT_MS = 8_000L
    }
}
