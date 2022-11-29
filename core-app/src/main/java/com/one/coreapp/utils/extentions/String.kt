package com.one.coreapp.utils.extentions

import android.util.Base64
import java.security.MessageDigest

fun encodeMd5(data: String): String {
    val md = MessageDigest.getInstance("MD5")
    val hashInBytes = md.digest(data.toByteArray())

    val sb = StringBuilder()
    for (b in hashInBytes) {
        sb.append(String.format("%02x", b))
    }
    return sb.toString()
}

fun encodeBase64(data: String): String {
    return String(Base64.encode(data.toByteArray(), Base64.DEFAULT))
}

fun decodeBase64(data: String): String {
    return String(Base64.decode(data.toByteArray(), Base64.DEFAULT))
}

fun byteArrayToBase64(data: ByteArray?): String {
    return Base64.encodeToString(data, Base64.DEFAULT)
}

fun base64ToByteArray(data: String?): ByteArray {
    return Base64.decode(data, Base64.DEFAULT)
}