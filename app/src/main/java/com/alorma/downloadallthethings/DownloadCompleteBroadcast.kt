package com.alorma.downloadallthethings

import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat

class DownloadCompleteBroadcast(private val retrieve: (Long) -> DownloadItem?,
                                private val removeItem: (Long) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        retrieve.invoke(referenceId)?.let {
            showNotification(context, referenceId, it)
        }
    }

    private fun showNotification(context: Context, refId: Long, downloadItem: DownloadItem) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("Downloads", "Downloads", NotificationManager.IMPORTANCE_DEFAULT).apply {
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val mBuilder = NotificationCompat.Builder(context, "Downloads")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Download ${downloadItem.title} completed")

        notificationManager.notify(refId.toInt(), mBuilder.build())

        removeItem.invoke(refId)
    }
}