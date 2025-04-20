package com.simple.adapter.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseBindingViewHolder<B : ViewBinding>(val binding: B, val viewType: Int) : RecyclerView.ViewHolder(binding.root)
