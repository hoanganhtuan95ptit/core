package com.simple.coreapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import java.io.File
import java.io.FileOutputStream

object FileUtils {

    fun uriToImageFile(context: Context, uri: Uri): File? {

        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val filePath = cursor.getString(columnIndex)
                cursor.close()
                return File(filePath)
            }
            cursor.close()
        }

        return null
    }


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

    fun delete(path: File) {
        path.delete()
    }
//    fun deletePrivateDownload(path: String) {
//        val directoryPrivateDownload =
//            BaseApp.shared.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: return
//
//        if (!path.contains(directoryPrivateDownload.absolutePath)) return
//
//        File(path).delete()
//    }

}