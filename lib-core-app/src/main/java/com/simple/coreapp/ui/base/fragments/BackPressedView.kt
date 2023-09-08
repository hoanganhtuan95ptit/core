package com.simple.coreapp.ui.base.fragments

interface BackPressedView {

    open fun onBackPressed(): Boolean {

        return false
    }
}