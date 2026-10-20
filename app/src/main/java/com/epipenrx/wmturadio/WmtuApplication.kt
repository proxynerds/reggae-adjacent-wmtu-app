package com.epipenrx.wmturadio

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class WmtuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createReminderChannel()
        ReminderScheduler.scheduleNext(this)
    }

    private fun createReminderChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            Constants.REMINDER_CHANNEL_ID,
            getString(R.string.reminder_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = getString(R.string.reminder_channel_description)
        }
        manager.createNotificationChannel(channel)
    }
}
