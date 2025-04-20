package com.simple.adapter

import com.simple.adapter.provider.AdapterProvider
import java.util.ServiceLoader


internal val provider: List<AdapterProvider> by lazy {

    ServiceLoader.load(AdapterProvider::class.java).toList()
}