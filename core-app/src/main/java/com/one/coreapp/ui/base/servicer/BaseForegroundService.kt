package com.one.coreapp.ui.base.servicer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.one.coreapp.R


abstract class BaseForegroundService : BaseService() {

    companion object {

        private const val TAG = "BaseForegroundService"

        fun <T : BaseForegroundService> startOrResume(context: Context, clazz: Class<T>) {
            val intent = Intent(context, clazz)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun <T : BaseForegroundService> pause(context: Context, clazz: Class<T>) {
            val intent = Intent(context, clazz)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun <T : BaseForegroundService> stop(context: Context, clazz: Class<T>) {
            context.stopService(Intent(context, clazz))
        }

        fun startForeground(service: Service) {

            val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(service, "my_service", "My Background Service")
            } else {
                ""
            }

            val notificationBuilder = NotificationCompat.Builder(service, channelId)
            val notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()

            service.startForeground(101, notification)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun createNotificationChannel(context: Context, channelId: String, channelName: String): String {

            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
            channel.lightColor = Color.BLUE
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)

            return channelId
        }
    }

    private var pause: Boolean = true

    override fun onCreate() {
        super.onCreate()
        startForeground(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return START_STICKY
    }

}