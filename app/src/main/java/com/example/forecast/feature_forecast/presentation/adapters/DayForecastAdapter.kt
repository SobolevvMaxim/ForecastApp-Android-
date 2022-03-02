package com.example.forecast.feature_forecast.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.domain.model.Hourly

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
                val temperature = "${it.temp}°"
                temperatureTV.text = temperature
                when (it.description) {
                    "Rain" -> image.setImageResource(R.drawable.forecast_rain_icon)
                    "Snow" -> image.setImageResource(R.drawable.forecast_snow_icon)
                    "Clear" -> image.setImageResource(R.drawable.forecast_sun_icon)
                    else -> image.setImageResource(R.drawable.forecast_clouds_icon)
                }
                date.text = getItemTime(position)
            }
        }
    }

    override fun getItemCount(): Int = forecast.size

    private fun getItemTime(position: Int): String {
        return "${(startTime + position) % 24}:00"
    }
}