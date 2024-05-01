package com.simple.coreapp.utils.extentions

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

fun ActivityResultLauncher<String>.launchTakeImageFromGallery() {
    launch("image/*")
}

fun ActivityResultLauncher<Intent>.launchTakeImageFromCamera(context: Context, imageName: String): File? {

    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    val photoFile: File?

    try {

        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null

        photoFile = File.createTempFile(imageName, ".jpg", storageDir)
    } catch (_: IOException) {

        return null
    }

    val photoURI = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
    launch(takePictureIntent)

    return photoFile
}