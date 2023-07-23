package com.one.coreapp.utils

import android.app.Activity
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.one.core.utils.extentions.toArrayList
import com.one.coreapp.BaseApp
import com.one.coreapp.R
import java.io.File
import java.util.*


object Utils {

    fun updateApp(activity: Activity) {
        try {

            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${activity.packageName}")))
        } catch (e: ActivityNotFoundException) {

            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${activity.packageName}")))
        }
    }

    fun openApp(activity: Activity, packageName: String) {
        val intent: Intent? = activity.packageManager.getLaunchIntentForPackage(packageName)

        if (intent != null) {

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } else kotlin.runCatching {

            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${packageName}")))
        }.getOrElse {

            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${packageName}")))
        }
    }

    @JvmStatic
    fun shareFile(activity: Activity, strings: List<String>) = shareFileList(activity, strings.map {
        File(it).provideUri()
    })

    @JvmStatic
    fun shareFile(activity: Activity, url: String) = shareFileList(activity, listOf(File(url).provideUri()))

    @JvmStatic
    private fun File.provideUri(): Uri {
        return FileProvider.getUriForFile(BaseApp.shared, BaseApp.shared.packageName + ".provider", this)
    }

    @JvmStatic
    private fun shareFileList(activity: Activity, files: List<Uri>) {
        val intent = Intent().apply {
            type = "*/*"
            action = Intent.ACTION_SEND_MULTIPLE

            putParcelableArrayListExtra(Intent.EXTRA_STREAM, files.toArrayList())
            putExtra(Intent.EXTRA_SUBJECT, "Create by " + BaseApp.shared.getString(R.string.app_name).toUpperCase(Locale.getDefault()))
        }
        activity.startActivity(intent)
    }


    fun call(context: Activity, text: String) = kotlin.runCatching {

        val intent = Intent(Intent.ACTION_DIAL)

        intent.data = Uri.parse("tel:$text")
        context.startActivity(intent)
    }


    fun export(context: Activity, text: String) = kotlin.runCatching {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(text))
        context.startActivity(intent)
    }

    fun copyText(context: Context, text: String) = kotlin.runCatching {

        Toast.makeText(context, "Copy $text", Toast.LENGTH_LONG).show()

        val clipboard: ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager ?: return@runCatching
        clipboard.setPrimaryClip(ClipData.newPlainText("label", text))
    }

    fun shareText(activity: Activity, text: String) = kotlin.runCatching {

        val intent = Intent().apply {
            type = "text/plain"
            action = Intent.ACTION_SEND

            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, "Share by " + BaseApp.shared.getString(R.string.app_name).uppercase(Locale.getDefault()))
        }

        activity.startActivity(intent)
    }

    @JvmStatic
    fun openBrowser(context: Context, link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }

    fun getAppName(): String = kotlin.runCatching {
        BaseApp.shared.getString(R.string.app_name)
    }.getOrElse {
        ""
    }

    fun getAppVersion(): String = kotlin.runCatching {
        BaseApp.shared.packageManager.getPackageInfo(BaseApp.shared.packageName, 0).versionName
    }.getOrElse {
        ""
    }


}