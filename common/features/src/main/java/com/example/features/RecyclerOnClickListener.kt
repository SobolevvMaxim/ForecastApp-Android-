package com.example.features

class RecyclerOnCLickListener<T>(
    val clickListener: (item: T) -> Unit,
    val onLongClickListener: (item: T) -> Unit
)