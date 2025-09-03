package com.unknown.size.uitls.exts

fun Map<String, Int>.getOrZero(key: String) = get(key) ?: 0

val Map<String, Int>.width: Int get() = getOrZero("width")
val Map<String, Int>.height: Int get() = getOrZero("height")
