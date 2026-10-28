package com.epipenrx.wmturadio

import android.content.Context

/**
 * When the user taps Play after having paused, do we pick up the live
 * stream from wherever ExoPlayer's in-memory buffer currently sits
 * (CONTINUE), or force a fresh connection at the live edge (RELOAD)?
 */
enum class ResumeMode {
    CONTINUE,
    RELOAD
}

object Prefs {
    private const val FILE = "wmtu_prefs"
    private const val KEY_RESUME_MODE = "resume_mode"
    private const val KEY_CACHE_MINUTES = "cache_minutes"

    const val MIN_CACHE_MINUTES = 1
    const val MAX_CACHE_MINUTES = 30
    const val DEFAULT_CACHE_MINUTES = 5

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun resumeMode(context: Context): ResumeMode {
        val stored = prefs(context).getString(KEY_RESUME_MODE, null) ?: return ResumeMode.CONTINUE
        return runCatching { ResumeMode.valueOf(stored) }.getOrDefault(ResumeMode.CONTINUE)
    }

    fun setResumeMode(context: Context, mode: ResumeMode) {
        prefs(context).edit().putString(KEY_RESUME_MODE, mode.name).apply()
    }

    fun cacheMinutes(context: Context): Int =
        prefs(context).getInt(KEY_CACHE_MINUTES, DEFAULT_CACHE_MINUTES)

    fun setCacheMinutes(context: Context, minutes: Int) {
        prefs(context).edit()
            .putInt(KEY_CACHE_MINUTES, minutes.coerceIn(MIN_CACHE_MINUTES, MAX_CACHE_MINUTES))
            .apply()
    }
}
