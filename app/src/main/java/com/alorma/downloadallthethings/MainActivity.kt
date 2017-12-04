package com.alorma.downloadallthethings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    companion object {
        val DOWNLOAD_URL: String = "https://github.com/alorma/DownloadAllTheThings/archive/master.zip"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}
