package com.example.forecast.feature_forecast.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.domain.model.Daily
import java.text.SimpleDateFormat
import java.util.*

class WeekForecastAdapter(
    private val forecast: List<Daily>,
    private val startDayIndex: Int,
) :
    RecyclerView.Adapter<WeekForecastAdapter.ViewHolder>() {
    private val dayOfWeeks = listOf("Понедельник", "Втоорник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье") // TODO: do as resource

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val temperatureTV: TextView = view.findViewById(R.id.item_temp)
        val descriptionTV: TextView = view.findViewById(R.id.item_description)
        val date: TextView = view.findViewById(R.id.week_day)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        forecast[position].let {
            holder.apply {
                val temperature = "${it.temp}°"
                temperatureTV.text = temperature
                descriptionTV.text = it.description
                date.text = getItemDate(position)
            }
        }
    }

    override fun getItemCount(): Int = forecast.size

    private fun getItemDate(position: Int): String {
        return dayOfWeeks[(startDayIndex + position - 1) % 7]
    }
}