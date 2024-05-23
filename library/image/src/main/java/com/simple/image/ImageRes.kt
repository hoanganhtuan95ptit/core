package com.simple.image

import com.simple.image.exts.Image


data class ImageRes(override val data: Int) : Image<Int>(data)

fun Int.toImage() = ImageRes(this)