package com.one.coreapp.utils.extentions

import android.graphics.Point
import android.util.Size
import java.util.*
import kotlin.Comparator
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

fun Size.toPoint(): List<Point> {
    val pointLeftTop = Point(0, 0)
    val pointLeftBottom = Point(0, height)

    val pointRightTop = Point(width, 0)
    val pointRightBottom = Point(width, height)

    return listOf(pointLeftTop, pointRightTop, pointRightBottom, pointLeftBottom)
}

fun List<Point>.sortPoints(): List<Point> {
    val result = arrayOf<Point?>(null, null, null, null)

    val sumComparator: java.util.Comparator<Point> = Comparator { lhs, rhs ->
        (lhs.y + lhs.x).compareTo(rhs.y + rhs.x)
    }

    val diffComparator: java.util.Comparator<Point> = Comparator { lhs, rhs ->
        (lhs.y - lhs.x).compareTo(rhs.y - rhs.x)
    }

    result[0] = Collections.min(this, sumComparator)

    result[2] = Collections.max(this, sumComparator)

    result[1] = Collections.min(this, diffComparator)

    result[3] = Collections.max(this, diffComparator)

    return result.filterNotNull()
}

fun List<Point>.rotate(size: Size, rotate: Int): List<Point> = map {
    it.rotate(Point(size.width / 2, size.height / 2), rotate)
}

fun List<Point>.rotate(size: Size, rotate: Double): List<Point> = map {
    it.rotate(Point(size.width / 2, size.height / 2), rotate)
}

fun List<Point>.rotate(v: Point, rotate: Int): List<Point> = map {
    it.rotate(v, rotate)
}

fun List<Point>.rotate(v: Point, rotate: Double): List<Point> = map {
    it.rotate(v, rotate)
}

fun Point.rotate(v: Point, rotate: Int): Point = rotate(v, rotate.toDouble())

fun Point.rotate(v: Point, rotate: Double): Point {
    val rotate = rotate * 3.14 / 180
    return Point(
        (x * cos(rotate) - y * sin(rotate) + v.x * (1 - cos(rotate)) + v.y * sin(rotate)).roundToInt(),
        (x * sin(rotate) + y * cos(rotate) - v.x * sin(rotate) + v.y * (1 - cos(rotate))).roundToInt()
    )
}

fun Point.flip(V: Point): Point = Point(x, -y + 2 * V.y)

fun Point.mirror(V: Point): Point = Point(-x + 2 * V.x, y)

fun List<Point>.wrap(ratio: Float): List<Point> = map {
    Point((it.x * ratio).roundToInt(), (it.y * ratio).roundToInt())
}

