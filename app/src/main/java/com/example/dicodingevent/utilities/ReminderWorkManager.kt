package com.example.dicodingevent.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dicodingevent.R
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ReminderWorkManager(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private var resultStatus: Result? = null

    override fun doWork(): Result {
//        val dataEvent = inputData.getString(EXTRA_EVENT)
        return getUpcomingEvent()
    }

    private fun getUpcomingEvent(): Result {
        Log.d(TAG, "getUpcomingEvent: Mulai.....")
        Looper.prepare()
        val client = SyncHttpClient()
        val url = "https://event-api.dicoding.dev/events?active=1&limit=1/"
        Log.d(TAG, "getUpcomingEvent: $url")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
            ) {
                val result = String(responseBody!!)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val upcomingEvent = responseObject.getJSONArray("listEvents").getJSONObject(0).getString("name")
                    val date = responseObject.getJSONArray("listEvents").getJSONObject(0).getString("beginTime")
                    val title = "Upcoming event $upcomingEvent"
                    val message = "Start on $date"
                    showNotification(title, message)
                    Log.d(TAG, "onSuccess: Selesai.....")
                    resultStatus = Result.success()
                } catch (e: Exception) {
                    showNotification("Get Upcoming Event Failed", e.message)
                    Log.d(TAG, "onSuccess: Gagal.....")
                    resultStatus = Result.failure()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?,
            ) {
                Log.d(TAG, "onFailure: Gagal.....")
                showNotification("Get Upcoming Event Failed", error?.message)
                resultStatus = Result.failure()
            }

        })
        return resultStatus as Result
    }

    private fun showNotification(title: String, message: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_event_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
//            notification.setChannelId(CHANNEL_ID)
//            notificationManager.createNotificationChannel(channel)
//        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        private val TAG = ReminderWorkManager::class.java.simpleName
        const val EXTRA_EVENT = "event"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding event channel"
    }
}