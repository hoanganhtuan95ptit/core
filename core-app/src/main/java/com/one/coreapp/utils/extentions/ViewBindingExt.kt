@file:Suppress("UNCHECKED_CAST")

package com.one.coreapp.utils.extentions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method

fun <VB : ViewBinding> Any.findBinding(view: View): VB {

    val method: Method = findGenericClassBySuperClass(ViewBinding::class.java)!!.java.getDeclaredMethod("bind", View::class.java)
    method.isAccessible = true

    return method.invoke(null, view) as VB
}

fun <VB : ViewBinding> Any.findBinding(parent: ViewGroup): VB {

    return findBinding(LayoutInflater.from(parent.context), parent) as VB
}

fun <VB : ViewBinding> Any.findBinding(inflater: LayoutInflater, container: ViewGroup? = null, attackToParent: Boolean = false): VB {

    val method: Method = findGenericClassBySuperClass(ViewBinding::class.java)!!.java.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    method.isAccessible = true

    return method.invoke(null, inflater, container, attackToParent) as VB
}
