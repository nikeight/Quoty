package com.appchefs.quoty.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.appchefs.quoty.R
import com.appchefs.quoty.data.remote.NetworkService
import com.appchefs.quoty.data.remote.Networking
import com.appchefs.quoty.utils.Constants
import javax.inject.Inject

class NotificationWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    lateinit var networkService: NetworkService

    override suspend fun doWork(): Result {

        networkService = Networking.create()
        val response = networkService.getRandomQuotes()

        return if (response.isSuccessful && response.body() != null) {
            val quoteContent = response.body()?.quoteContent
            val author = response.body()?.author
            sendNotification(quoteContent!!,author!!)
            Log.i("WorkManager", "Success")
            Result.success()
        } else {
            Log.i("WorkManager", "Error")
            Result.failure()
        }
    }

    private fun sendNotification(quoteContent: String, author: String) {

        val notificationManager =
            applicationContext
                .getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        val notificationTitle = "Quote of the day by $author"

        val notification = NotificationCompat.Builder(
            applicationContext,
            Constants.QUOTE_NOTIFICATION_CHANNEL
        ).setContentTitle(notificationTitle)
            .setContentText(quoteContent)
            .setSmallIcon(R.drawable.ic_quote_vector)
            .setStyle(NotificationCompat.BigTextStyle())
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(Constants.QUOTE_NOTIFICATION_CHANNEL)

            val quoteChannel = NotificationChannel(
                Constants.QUOTE_NOTIFICATION_CHANNEL,
                Constants.QUOTE_NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager.createNotificationChannel(quoteChannel)
        }

        notificationManager.notify(Constants.QUOTE_NOTIFICATION_ID, notification.build())
    }
}


