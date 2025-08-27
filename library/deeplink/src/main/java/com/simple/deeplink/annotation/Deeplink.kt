package com.simple.deeplink.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Deeplink(val queue: String = "Deeplink")