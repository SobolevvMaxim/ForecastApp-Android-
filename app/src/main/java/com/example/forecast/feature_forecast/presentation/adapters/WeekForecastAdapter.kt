package com.example.forecast.feature_forecast.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.domain.data_processing.Extensions.getCelsius
import com.example.forecast.domain.model.Daily
import com.example.forecast.feature_forecast.presentation.utils.Utils.getForecastImageID

class WeekForecastAdapter(
    private val forecast: List<Daily>,
    private val startDayIndex: Int,
    private val dayOfWeeks: List<String>,
) :
    RecyclerView.Adapter<WeekForecastAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val temperatureTV: TextView = view.findViewById(R.id.daily_temp)
        val descriptionTV: TextView = view.findViewById(R.id.daily_description)
        val date: TextView = view.findViewById(R.id.daily_date)
        val image: ImageView = view.findViewById(R.id.daily_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_forecast_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        forecast[position].let {
            holder.apply {
                temperatureTV.text = it.temp.getCelsius()
                image.setImageResource(it.description.getForecastImageID())
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