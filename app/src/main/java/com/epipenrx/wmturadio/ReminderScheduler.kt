package com.epipenrx.wmturadio

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

/**
 * Schedules the next "show starting" alarm and re-arms itself each time it
 * fires (rather than AlarmManager.setRepeating), so DST and clock changes
 * don't drift the Monday 6pm trigger.
 */
object ReminderScheduler {

    fun scheduleNext(context: Context) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
        val triggerAt = nextShowTimeMillis()

        val intent = Intent(context, ShowReminderReceiver::class.java).apply {
            action = Constants.ACTION_SHOW_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Constants.REMINDER_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val canScheduleExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
            alarmManager.canScheduleExactAlarms()

        if (canScheduleExact) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        } else {
            // Exact-alarm permission not granted -- fall back to an inexact
            // alarm. Still fires close enough for a "show is starting" nudge.
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
        }
    }

    private fun nextShowTimeMillis(): Long {
        val now = Calendar.getInstance()
        val candidate = (now.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, Constants.SHOW_HOUR_24)
            set(Calendar.MINUTE, Constants.SHOW_MINUTE)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        while (candidate.get(Calendar.DAY_OF_WEEK) != Constants.SHOW_DAY_OF_WEEK ||
            !candidate.after(now)
        ) {
            candidate.add(Calendar.DAY_OF_MONTH, 1)
        }
        return candidate.timeInMillis
    }
}
