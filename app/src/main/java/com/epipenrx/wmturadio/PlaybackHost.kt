package com.epipenrx.wmturadio

import androidx.media3.common.Player
import androidx.media3.session.MediaController

/**
 * MainActivity owns the single MediaController for the app's lifetime and
 * implements this so PlayFragment/OptionsFragment can drive playback
 * without each holding their own controller connection.
 */
interface PlaybackHost {
    fun currentController(): MediaController?
    fun addPlaybackListener(listener: Player.Listener)
    fun removePlaybackListener(listener: Player.Listener)

    /**
     * Fires [callback] with the controller as soon as one exists -- right
     * away if it's already built, or once MediaController.Builder's async
     * connect finishes otherwise. Call again after each onViewCreated since
     * a fresh fragment view can't assume the controller from a previous one
     * is still connected (e.g. after the Activity was backgrounded).
     */
    fun onControllerReady(callback: (MediaController) -> Unit)

    /** Tap-to-play, honoring the user's resume-mode preference. */
    fun playRespectingPreference()

    /**
     * Force a fresh connection to the live stream (pull-to-refresh, and
     * Play when the RELOAD preference is set). [onSettled] fires once the
     * player reaches READY or hits an error, or after a timeout -- whichever
     * comes first -- so callers can stop a refresh spinner.
     */
    fun reloadStream(onSettled: () -> Unit)
}
