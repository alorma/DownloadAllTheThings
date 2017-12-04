package com.alorma.downloadallthethings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        val DOWNLOAD_URL: String = "https://github.com/alorma/DownloadAllTheThings/archive/master.zip"
        val IMAGE_URL: String = "https://avatars2.githubusercontent.com/u/887462?s=460&v=4"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}
