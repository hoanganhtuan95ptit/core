package com.one.coreapp.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import com.one.core.utils.extentions.toJson
import com.one.coreapp.App
import com.one.coreapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException

object FileUtils {

    fun save(context: Context, bitmap: Bitmap, name: String? = null, url: String = ""): File {

        val nameWrap = name ?: (System.currentTimeMillis().toString() + getName(url))

        return save(bitmap, createFileImage(context, true, nameWrap)!!)
    }

    fun save(result: Bitmap, file: File): File {
        val fos = FileOutputStream(file)
        result.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        return file
    }

    fun createFileImage(context: Context, private: Boolean, name: String): File? {

        return createFile(context, private, "images", name)
    }

    fun createFile(context: Context, private: Boolean, folderName: String, name: String): File? {

        val directory = if (private) {
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        }

        if (directory?.exists() != true) return null

        return createFile(directory.path, folderName, name)
    }

    fun createFile(dirPath: String, folderName: String, name: String): File? {
        val directory = createDirectory(dirPath, folderName)

        if (directory?.exists() != true) return null

        val file = File(directory, name)

        return if (file.exists() || file.createNewFile()) {
            if (BuildConfig.DEBUG) Log.d("Đã tạo file: %s", file.absolutePath)
            file
        } else {
            null
        }
    }

    fun createDirectory(dirPath: String, folderName: String): File? {
        val directory = if (folderName.isNotEmpty()) {
            File(dirPath, folderName)
        } else {
            File(dirPath)
        }

        return if (directory.exists() || directory.mkdirs()) {
            if (BuildConfig.DEBUG) Log.d("Đã tạo folder: %s", directory.absolutePath)
            directory
        } else {
            null
        }
    }

    fun getName(url: String): String {
        return URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url))
    }

    fun delete(path: String) {
        File(path).delete()
    }

    fun deletePrivateDownload(path: String) {
        val directoryPrivateDownload =
            App.shared.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: return

        if (!path.contains(directoryPrivateDownload.absolutePath)) return

        File(path).delete()
    }

}