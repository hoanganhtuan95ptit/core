package com.simple.adapter

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class MultiRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    init {

        adapter = MultiAdapter()
    }
}