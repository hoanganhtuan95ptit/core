package com.one.detect.entities

enum class DetectOption(val value: Int) {

    TEXT(0),
    TEXT_TRANSLATE(1),
    OBJECT(2),
    OBJECT_TRANSLATE(3),
    HANDWRITING(4),
    HANDWRITING_TRANSLATE(5);

}

fun DetectOption.isCanTranslate() = this in listOf(DetectOption.TEXT_TRANSLATE, DetectOption.OBJECT_TRANSLATE, DetectOption.HANDWRITING_TRANSLATE)

fun DetectOption.isDetectText() = this in listOf(DetectOption.TEXT, DetectOption.TEXT_TRANSLATE, DetectOption.HANDWRITING, DetectOption.HANDWRITING_TRANSLATE)

fun Int.toDetectOption(): DetectOption = when (this) {
    DetectOption.TEXT.value -> DetectOption.TEXT
    DetectOption.TEXT_TRANSLATE.value -> DetectOption.TEXT_TRANSLATE
    DetectOption.OBJECT.value -> DetectOption.OBJECT
    DetectOption.OBJECT_TRANSLATE.value -> DetectOption.OBJECT_TRANSLATE
    DetectOption.HANDWRITING.value -> DetectOption.HANDWRITING
    DetectOption.HANDWRITING_TRANSLATE.value -> DetectOption.HANDWRITING_TRANSLATE
    else -> DetectOption.TEXT_TRANSLATE
}
