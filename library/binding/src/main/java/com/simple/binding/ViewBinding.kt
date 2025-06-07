@file:Suppress("UNCHECKED_CAST")

package com.simple.binding

import android.content.ComponentCallbacks
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.simple.core.utils.extentions.findGenericClassBySuperClass
import java.lang.reflect.Method

fun <VB : ViewBinding> ComponentCallbacks.findBinding(view: View): VB {

    return findGenericClassBySuperClass(ViewBinding::class.java)!!.java.findBinding(view)
}

fun <VB : ViewBinding> ComponentCallbacks.findBinding(parent: ViewGroup): VB {

    return findGenericClassBySuperClass(ViewBinding::class.java)!!.java.findBinding(LayoutInflater.from(parent.context), parent, false)
}

fun <VB : ViewBinding> ComponentCallbacks.findBinding(inflater: LayoutInflater, container: ViewGroup? = null, attackToParent: Boolean = false): VB {

    return findGenericClassBySuperClass(ViewBinding::class.java)!!.java.findBinding(inflater, container, attackToParent)
}


fun <VB : ViewBinding> Class<ViewBinding>.findBinding(parent: ViewGroup): VB {

    return findBinding(LayoutInflater.from(parent.context), parent) as VB
}


fun <VB : ViewBinding> Class<ViewBinding>.findBinding(view: View): VB {

    val method: Method = this.getDeclaredMethod("bind", View::class.java)
    method.isAccessible = true

    return method.invoke(null, view) as VB
}

fun <VB : ViewBinding> Class<ViewBinding>.findBinding(inflater: LayoutInflater, container: ViewGroup? = null, attackToParent: Boolean = false): VB {

    val method: Method = this.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    method.isAccessible = true

    return method.invoke(null, inflater, container, attackToParent) as VB
}