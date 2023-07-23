package com.one.coreapp.ui.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.one.coreapp.BaseApp
import com.one.coreapp.R
import com.one.coreapp.data.api.retrofit.ConfigApi
import com.one.coreapp.data.cache.NotificationCache
import com.one.analytics.logAnalytics
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class NotificationDateWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams), KoinComponent {

    private val configApi: ConfigApi by inject()

    private val notificationCache: NotificationCache by inject()

    override suspend fun doWork(): Result {

        if (BaseApp.numStart != 0) {

            return Result.retry()
        }

        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val date = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)


        val config = configApi.fetchConfig()

        val notification = config.notifications.filter { notification ->

            notification.timeRanges.mapNotNull { timeRange ->
                timeRange.split("-").mapNotNull { it.toIntOrNull() }.takeIf { it.size == 2 }
            }.any {
                hour in it[0]..it[1]
            }
        }.associateBy {

            it.languageCode
        }.let {

            it[Locale.getDefault().language] ?: it["en"]
        }?.takeIf {

            notificationCache.getTimeShow(it.id).toIntOrNull() != date
        }.let {

            it ?: return Result.retry()
        }


        notificationCache.saveTimeShow(notification.id, "$date")


        val appName = applicationContext.getString(R.string.app_name)


        val title = notification.title

        val message = notification.messages.random()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(appName, appName, NotificationManager.IMPORTANCE_DEFAULT).apply {
                setSound(null, null)
                enableVibration(true)
            }

            NotificationManagerCompat.from(applicationContext).createNotificationChannel(channel)
        }


        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            PendingIntent.getActivity(applicationContext, 0, applicationContext.packageManager.getLaunchIntentForPackage(applicationContext.packageName), PendingIntent.FLAG_IMMUTABLE)
        } else {

            PendingIntent.getActivity(applicationContext, 0, applicationContext.packageManager.getLaunchIntentForPackage(applicationContext.packageName), 0)
        }

        val builder = NotificationCompat.Builder(applicationContext, appName)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        NotificationManagerCompat.from(applicationContext).notify(0, builder.build().apply {
            this.contentIntent = intent
        })

        logAnalytics("notification", "")

        return Result.retry()
    }


    companion object {

        fun schedule(repeatInterval: Long = 1, repeatIntervalTimeUnit: TimeUnit = TimeUnit.HOURS) {

            val constraint: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val work: WorkRequest = PeriodicWorkRequestBuilder<NotificationDateWorker>(repeatInterval, repeatIntervalTimeUnit)
                .setConstraints(constraint)
                .addTag("test")
                .build()

            WorkManager.getInstance(BaseApp.shared).let {
                it.cancelAllWorkByTag("test")
                it.enqueue(work)
            }

            NotificationManagerCompat.from(BaseApp.shared).cancelAll()
        }
    }

}