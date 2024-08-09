package com.example.d_clock

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
class RunningService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start () {
        val notification = NotificationCompat.Builder(this, "running_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Time")
            .setContentText(getCurrentTime())
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
        startForeground(1, notification)
    }

    enum class Actions {
        START, STOP
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}*/
import java.util.*
var time = mutableStateOf("00:00:00")
var alarm = mutableStateOf(false)

class RunningService : Service() {
    private lateinit var timer: Timer
    private lateinit var notificationManager: NotificationManager

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = if (!alarm.value) createNotification() else createAlarmNotification()
        startForeground(if (!alarm.value) 1 else 2, notification)

        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                updateNotification()
            }
        }, 0, 1000)
    }

    private fun createNotification(): android.app.Notification {
        return NotificationCompat.Builder(this, "running_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Time")
            .setContentText(getCurrentTime())
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun createAlarmNotification () : android.app.Notification {
        return NotificationCompat.Builder(this, "running_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Alarm")
            .setContentText("in ${calculateTimeDifference(time.value)}")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    fun calculateTimeDifference(givenTimeString: String): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val currentDate = Calendar.getInstance()
        val givenDate = Calendar.getInstance()

        try {
            val givenTime = dateFormat.parse(givenTimeString)
            givenDate.time = givenTime ?: return "Invalid time format"

            givenDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR))
            givenDate.set(Calendar.MONTH, currentDate.get(Calendar.MONTH))
            givenDate.set(Calendar.DAY_OF_MONTH, currentDate.get(Calendar.DAY_OF_MONTH))

            val diffMillis = currentDate.timeInMillis - givenDate.timeInMillis

            val hours = diffMillis / (60 * 60 * 1000)
            val minutes = (diffMillis % (60 * 60 * 1000)) / (60 * 1000)
            val seconds = (diffMillis % (60 * 1000)) / 1000

            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    private fun updateNotification() {
        val notification = if (!alarm.value) createNotification() else createAlarmNotification()
        notificationManager.notify(if (!alarm.value) 1 else 2, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::timer.isInitialized) {
            timer.cancel()
        }
    }

    enum class Actions {
        START, STOP
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
