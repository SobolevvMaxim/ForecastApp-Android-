package com.example.homeworksandroid.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.homeworksandroid.CityWeather

class HistoryDiffUtilCallback(
    private val oldList: Set<CityWeather>,
    private val newList: Set<CityWeather>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldQuery = oldList.elementAt(oldItemPosition)
        val newQuery = newList.elementAt(newItemPosition)
        return oldQuery == newQuery
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldQuery = oldList.elementAt(oldItemPosition)
        val newQuery = newList.elementAt(newItemPosition)
        return oldQuery == newQuery
    }
}