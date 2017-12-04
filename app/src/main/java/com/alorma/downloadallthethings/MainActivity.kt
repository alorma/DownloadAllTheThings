package com.alorma.downloadallthethings

import android.Manifest
import android.app.DownloadManager
import android.content.Context
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

    private var itemToDownload: DownloadItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }

    private fun download(item: DownloadItem) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(item.url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(item.title)
        request.setDescription("Downloading ${item.title}")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/DownloadAllTheThings/${item.filename}")

        downloadManager.enqueue(request)
    }

    private fun hasPermission(): Boolean =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission(item: DownloadItem) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            itemToDownload = item;
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), permissionRequest)
        } else {
            download(item)
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
}
