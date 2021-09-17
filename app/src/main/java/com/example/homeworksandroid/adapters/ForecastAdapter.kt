package com.example.homeworksandroid.adapters

import android.icu.util.Calendar
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.homeworksandroid.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ForecastAdapter(private val forecast: ArrayList<Pair<Int, String>>) :
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        forecast[position].let {
            holder.apply {
                temperatureTV.text = it.first.toString()
                descriptionTV.text = it.second
                date.text = LocalDateTime.now().plusDays(position.toLong() + 1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            }
        }
    }

    override fun getItemCount(): Int = forecast.size
}