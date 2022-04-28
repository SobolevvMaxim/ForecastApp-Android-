package com.example.features

class RecyclerClickListener<T>(
    val clickListener: ((item: T) -> Unit)? = null,
    val onLongClickListener: ((item: T) -> Unit)? = null,
)