package com.example.homeworksandroid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homeworksandroid.Daily
import com.example.homeworksandroid.FORMAT
import com.example.homeworksandroid.R
import java.util.*
import kotlin.collections.ArrayList

class ForecastAdapter(
    private val forecast: ArrayList<Daily>,
    private val fromDateTime: Date
) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val temperatureTV: TextView = view.findViewById(R.id.item_temp)
        val descriptionTV: TextView = view.findViewById(R.id.item_discription)
        val date: TextView = view.findViewById(R.id.item_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        forecast[position].let {
            holder.apply {
                temperatureTV.text = it.temp.toString()
                descriptionTV.text = it.description
                date.text = getItemDate(position)
            }
        }
    }

    override fun getItemCount(): Int = forecast.size

    private fun getItemDate(position: Int): String {
        val calendar = Calendar.getInstance()

        calendar.apply {
            time = fromDateTime
            add(Calendar.DATE, position + 1)
        }

        return FORMAT.format(calendar.time)
    }
}