package com.epipenrx.wmturadio

object Constants {
    const val STREAM_URL = "https://stream.wmtu.fm/wmtu-live"
    const val STATION_NAME = "WMTU 91.9 FM"

    const val SHOW_NAME = "Reggae Adjacent with DJ TomASS"
    // Calendar.MONDAY, 24-hour local time.
    const val SHOW_DAY_OF_WEEK = java.util.Calendar.MONDAY
    const val SHOW_HOUR_24 = 18
    const val SHOW_MINUTE = 0

    const val REMINDER_CHANNEL_ID = "show_reminders"
    // Must not collide with Media3's own "now playing" notification, which
    // defaults to id 1001 (androidx.media3.session.MediaNotification) -- a
    // real collision was found during testing: this id posting over the
    // active playback notification just got silently clobbered right back
    // by the session's own notification using the same id.
    const val REMINDER_NOTIFICATION_ID = 5001

    const val ACTION_SHOW_REMINDER = "com.epipenrx.wmturadio.ACTION_SHOW_REMINDER"
    const val REMINDER_REQUEST_CODE = 2001

    // Window after a route-change pause (BT disconnect) during which we'll
    // auto-resume on the next device that connects -- mirrors the 15s window
    // adopted for audiobookshelf-app's BT auto-resume feature (PR #1914).
    const val BT_RESUME_WINDOW_MS = 15_000L

    const val WMTU_WEBSITE_URL = "https://wmtu.fm"
    const val WMTU_ARCHIVES_URL = "https://wmtu.fm/archives"
    const val MTU_WEBSITE_URL = "https://www.mtu.edu"

    // Used only for the Options screen's "approx memory" buffer estimate.
    // Measured directly against the live stream 2026-10-28 (two curl samples,
    // ~45 KB/s steady-state each) rather than assumed from a typical bitrate,
    // since this stream runs noticeably higher than a standard 128kbps feed.
    // Re-measure if WMTU ever changes their encoding.
    const val STREAM_APPROX_BYTES_PER_MINUTE = 2_700_000L

    const val CONTACT_EMAIL = "proxynerds911@gmail.com"
    // Hosted copy published 2026-10-28 (see docs/PLAY_STORE_LISTING.md). It's
    // a private-by-default Claude Artifact -- share it (its page's Share
    // menu) before pointing Play Console at it, or move it to a more
    // permanent host like GitHub Pages and update this URL.
    const val PRIVACY_POLICY_URL = "https://claude.ai/artifact/3js9ukEwabjbTWBRCRGWxx"
}
