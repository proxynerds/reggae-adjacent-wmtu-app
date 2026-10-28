package com.epipenrx.wmturadio

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

/**
 * Owns the ExoPlayer instance and MediaSession for the live stream.
 * Extending MediaSessionService (rather than hand-rolling a
 * NotificationManager + PlayerNotificationManager pairing, the way
 * audiobookshelf-app originally did) gets the shade notification and its
 * correct startForeground/stopForeground lifecycle for free -- that pairing
 * is exactly where ABS had a real bug (see abs_android_fix.md: a missing
 * stopForeground branch on pause, and on notification cancellation, each
 * left the service in a broken foreground state). Media3 handles both
 * transitions internally.
 */
class PlaybackService : MediaSessionService() {

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession
    private lateinit var bluetoothRouteHandler: BluetoothRouteHandler

    override fun onCreate() {
        super.onCreate()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        // Back buffer is the "cache" the Options screen's buffer setting
        // controls: how much already-played live audio ExoPlayer keeps in
        // memory (never written to disk) so tapping Play while Resume mode
        // is CONTINUE can pick up mid-buffer instead of jumping to live.
        // Read once at service creation -- a running player's LoadControl
        // can't be swapped live, so a changed value takes effect next time
        // the stream reloads/restarts (PlaybackHost.reloadStream, or a
        // fresh service instance).
        val loadControl = DefaultLoadControl.Builder()
            .setBackBuffer(
                Prefs.cacheMinutes(this) * 60_000,
                /* retainBackBufferFromKeyframe= */ true
            )
            .build()

        player = ExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .setAudioAttributes(audioAttributes, /* handleAudioFocus= */ true)
            .setHandleAudioBecomingNoisy(true)
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(Constants.STREAM_URL)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(Constants.STATION_NAME)
                    .setArtist(getString(R.string.live_stream_label))
                    .build()
            )
            .setLiveConfiguration(MediaItem.LiveConfiguration.Builder().build())
            .build()
        player.setMediaItem(mediaItem)
        player.prepare()

        val sessionActivity = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(sessionActivity)
            .build()

        bluetoothRouteHandler = BluetoothRouteHandler(this, player)
        bluetoothRouteHandler.start()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        bluetoothRouteHandler.stop()
        mediaSession.release()
        player.release()
        super.onDestroy()
    }
}
