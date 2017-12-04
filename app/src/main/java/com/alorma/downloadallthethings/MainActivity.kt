package com.alorma.downloadallthethings

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val permissionRequest: Int = 1121

        val DOWNLOAD_URL = DownloadItem("https://github.com/alorma/DownloadAllTheThings/archive/master.zip", "Repo", "repo.zip")
        val IMAGE_URL = DownloadItem("https://avatars2.githubusercontent.com/u/887462?s=460&v=4", "Image", "avatar.jpg")
    }

    private val map = mutableMapOf<Long, DownloadItem>()

    private var itemToDownload: DownloadItem? = null
    private val broadcast = DownloadCompleteBroadcast({ map[it] }, { map.remove(it) })

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
            if (hasPermission()) {
                download(DOWNLOAD_URL)
            } else {
                requestPermission(DOWNLOAD_URL)
            }
        }

        downloadImage.setOnClickListener {
            if (hasPermission()) {
                download(IMAGE_URL)
            } else {
                requestPermission(IMAGE_URL)
            }
        }

        downloadAll.setOnClickListener {
            if (hasPermission()) {
                download(DOWNLOAD_URL)
                download(IMAGE_URL)
            } else {
                requestPermission()
            }
        }
    }

    private fun download(item: DownloadItem) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(item.url)).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            setTitle(item.title)
            setDescription("Downloading ${item.title}")
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/DownloadAllTheThings/${item.filename}")
        }

        val refId = downloadManager.enqueue(request)
        map.put(refId, item)
    }

    private fun hasPermission(): Boolean =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission(item: DownloadItem? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            itemToDownload = item
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), permissionRequest)
        } else {
            item?.let {
                download(it)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest) {
            if (grantResults.isNotEmpty()) {
                itemToDownload?.let {
                    download(it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcast)
    }
}
