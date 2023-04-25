package com.one.coreapp.ui.base.fragments

interface BackPressedView {

    open fun onBackPressed(): Boolean {

        return false
    }
}