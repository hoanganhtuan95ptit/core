package com.simple.coreapp.entities

enum class ToastType {
    SUCCESS, ERROR, WARNING, INFO;

    companion object {

        fun String.toToastType(): ToastType? = entries.find { it.name == this }
    }
}
