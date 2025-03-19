package com.almax.stopwatch

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.almax.stopwatch.ui.stopwatch.StopWatchActivity
import com.almax.stopwatch.utils.formatTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StopWatchService : Service() {

    private var timer = 0L

    private var isRunning: Boolean = false

    private var startTime = 0L

    private val binder: IBinder = StopWatchBinder()

    private lateinit var notificationBuilder: NotificationCompat.Builder

    inner class StopWatchBinder : Binder() {
        val service = this@StopWatchService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun startTimer(): Flow<Long> {
        isRunning = true
        startTime = System.currentTimeMillis() - timer
        return flow {
            while (isRunning) {
                timer = System.currentTimeMillis() - startTime
                emit(timer)
                delay(10L)
                updateNotification(timer)
            }
        }
    }

    private fun updateNotification(timer: Long) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder.setContentText(formatTime(timer))
            .setSilent(true)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    fun stopTimer() {
        isRunning = false
    }

    fun resetTimer() {
        isRunning = false
        timer = 0L
        stopSelf()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: service created")
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: service started")
        return START_STICKY
    }

    private fun startForegroundService() {
        notificationBuilder = createNotificationBuilder()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel(): NotificationChannel {
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager =
            ContextCompat.getSystemService(this, NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(channel)
        return channel
    }

    private fun createNotificationBuilder(): NotificationCompat.Builder {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText("")
            .setContentTitle("Stop Watch")
            .setContentIntent(getPendingIntent())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)

        return notificationBuilder
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, StopWatchActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: service stopped")
    }

    companion object {
        private const val NOTIFICATION_ID = 100
        private const val CHANNEL_ID = "stop_watch_id"
        private const val CHANNEL_NAME = "stop_watch_channel"
    }
}

const val TAG = "StopWatchServiceTAG"