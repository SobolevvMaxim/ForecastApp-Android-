package com.example.forecast.feature_forecast.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.extensions.UIUtils.getTemperature
import com.example.forecast.R
import com.example.forecast.domain.model.Hourly
import com.example.forecast.feature_forecast.presentation.utils.Utils.getForecastImageID

class DayForecastAdapter(
    private val forecast: List<Hourly>,
    private val startTime: Int,
) :
    RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val temperatureTV: TextView = view.findViewById(R.id.hourly_temp)
        val date: TextView = view.findViewById(R.id.hourly_date)
        val image: ImageView = view.findViewById(R.id.hourly_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hourly_forecast_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        forecast[position].let {
            holder.apply {
                temperatureTV.text = it.temp.getTemperature()
                image.setImageResource(it.description.getForecastImageID())
                date.text = getItemTime(position)
            }
        }
    }

    override fun getItemCount(): Int = forecast.size

    private fun getItemTime(position: Int): String {
        return "${(startTime + position) % 24}:00"
    }
}