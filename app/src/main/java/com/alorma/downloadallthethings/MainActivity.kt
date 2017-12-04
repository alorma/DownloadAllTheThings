package com.alorma.downloadallthethings

import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    companion object {
        val DOWNLOAD_URL = DownloadItem("https://github.com/alorma/DownloadAllTheThings/archive/master.zip", "Repo", "repo.zip")
        val IMAGE_URL = DownloadItem("https://avatars2.githubusercontent.com/u/887462?s=460&v=4", "Image", "avatar.jpg")
    }

    private val broadcast = DownloadCompleteBroadcast()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addBroadcast()
        buildButtons()
    }

    private fun addBroadcast() {
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(broadcast, intentFilter)
    }

    private fun buildButtons() {
        downloadZip.setOnClickListener {
            download(DOWNLOAD_URL)
        }

        downloadImage.setOnClickListener {
            download(IMAGE_URL)
        }

        downloadAll.setOnClickListener {
            download(DOWNLOAD_URL)
            download(IMAGE_URL)
        }
    }

    private fun download(item: DownloadItem) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val externalCacheDir = externalCacheDir

        val file = File(externalCacheDir.absolutePath + "/downloads/${item.filename}")

        val request = DownloadManager.Request(Uri.parse(item.url)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setTitle(item.title)
            setDescription("Downloading ${item.title}")
            setDestinationUri(Uri.fromFile(file))
        }

        val refId = downloadManager.enqueue(request)
        broadcast.addDownload(refId, item)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcast)
    }
}
