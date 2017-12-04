package com.alorma.downloadallthethings

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
        val mBuilder = NotificationCompat.Builder(context, "Downloads")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Download ${downloadItem.title} completed")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(refId.toInt(), mBuilder.build())

        removeItem.invoke(refId)
    }
}