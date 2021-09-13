package com.example.homeworksandroid.adapters

import android.text.method.TextKeyListener.clear
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.R
import java.util.*
import kotlin.collections.LinkedHashSet

class CitiesAdapter(private val cities: LinkedHashSet<CityWeather>) :
    RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_city)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val string =
            cities.elementAt(position).name + ", " + cities.elementAt(position).country.uppercase(
                Locale.getDefault()
            )
        holder.textView.text = string
    }

    fun updateValues(newValues: LinkedHashSet<CityWeather>) {
        val historyDiffUtilCallback = HistoryDiffUtilCallback(cities, newValues)
        val historyDiffResult = DiffUtil.calculateDiff(historyDiffUtilCallback, true)
        cities.clear()
        cities.addAll(newValues)
        historyDiffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = cities.size
}