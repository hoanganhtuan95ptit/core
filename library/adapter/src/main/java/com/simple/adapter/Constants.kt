package com.simple.adapter

import com.hoanganhtuan95ptit.autobind.AutoBind
import com.simple.adapter.provider.AdapterProvider


internal val provider: List<AdapterProvider> by lazy {

    AutoBind.load(AdapterProvider::class.java, false)
}