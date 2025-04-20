package com.simple.deeplink.provider

import com.simple.deeplink.DeeplinkHandler

abstract class DeeplinkProvider {
    abstract fun provider(): List<Pair<String, DeeplinkHandler>>
}
