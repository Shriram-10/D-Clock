package com.example.d_clock

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class TimeNotificationService (private val context : Context) {
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    fun showNotification () {
        val notification = NotificationCompat.Builder (context, "time_notification_channel")
            .setContentTitle("Time")
            .setContentText(getCurrentTime())
            .setSmallIcon(R.drawable.baseline_access_time_24)
            .setPriority(NotificationManager.IMPORTANCE_MAX)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()

        notificationManager.notify(1, notification)
    }

    fun deleteNotification () {
        notificationManager.cancelAll()
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}