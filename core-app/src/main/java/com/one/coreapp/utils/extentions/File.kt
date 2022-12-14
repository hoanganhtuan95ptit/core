package com.one.core.utils.extentions

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import com.one.coreapp.App
import com.one.coreapp.utils.extentions.resumeActive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileWriter
import java.io.IOException

fun File.saveSync(text: Any? = null) {

    saveSync(text.toJson())
}

fun File.saveSync(text: String? = "") {

    var fileWriter: FileWriter? = null

    try {

        fileWriter = FileWriter(this, false)
        fileWriter.write(text)
    } catch (e: IOException) {

        throw RuntimeException("IOException occurred. ", e)
    } finally {

        fileWriter?.close()
    }
}

suspend fun File.downloadSync(url: String, title: String? = null) = suspendCancellableCoroutine<Boolean> {

    if (this@downloadSync.exists()) {

        it.resumeActive(true)
        return@suspendCancellableCoroutine
    }

    GlobalScope.launch(Dispatchers.Main) {

        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(title ?: "YOUR TITLE")
        request.setDescription(url.substring(url.length - 20, url.length))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationUri(Uri.fromFile(this@downloadSync))

        val manager = App.shared.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadId = manager.enqueue(request)


        App.shared.registerReceiver(object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {

                val id = intent!!.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

                if (downloadId == id) {
                    it.resumeActive(true)
                }
            }
        }, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
}

fun String.getFile(createFile: Boolean): File {

    val file = File(App.shared.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath + "/" + this)

    file.parentFile?.parentFile?.parentFile?.parentFile?.parentFile?.takeIf { !it.exists() }?.mkdirs()
    file.parentFile?.parentFile?.parentFile?.parentFile?.takeIf { !it.exists() }?.mkdirs()
    file.parentFile?.parentFile?.parentFile?.takeIf { !it.exists() }?.mkdirs()
    file.parentFile?.parentFile?.takeIf { !it.exists() }?.mkdirs()
    file.parentFile?.takeIf { !it.exists() }?.mkdirs()

    if (createFile && !file.exists()) {
        file.createNewFile()
    }

    return file
}

