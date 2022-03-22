package com.example.forecast.feature_forecast.presentation.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.features.RecyclerOnCLickListener
import com.example.forecast.R
import com.example.forecast.domain.data_processing.DataProcessing
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.utils.ChosenCityInterface
import kotlinx.android.synthetic.main.city_item.view.*

class CitiesRecyclerAdapter(
    private val listener: RecyclerOnCLickListener<CityWeather>,
    private var chosenID: String,
    private val highlightColor: String,
) :
    ListAdapter<CityWeather, CitiesRecyclerAdapter.ViewHolder>(diffUtilCallback),
    ChosenCityInterface {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cityTV: TextView = view.item_city
        private val temperatureTV: TextView = view.city_temperature

        @SuppressLint("ResourceAsColor")
        fun bind(
            item: CityWeather,
            listener: RecyclerOnCLickListener<CityWeather>,
            chosenCityInterface: ChosenCityInterface,
            highlightColor: String,
        ) = with(itemView) {
            DataProcessing(item).apply {
                cityTV.text = getForecastLocation()
                temperatureTV.text = getTemperature()
            }

            highlightIfChosenCity(itemID = item.id, chosenCityInterface, highlightColor)

            setOnClickListener {
                listener.clickListener(item)
            }
            setOnLongClickListener {
                listener.onLongClickListener(item)
                true
            }
        }

        private fun highlightIfChosenCity(
            itemID: String,
            chosenCityInterface: ChosenCityInterface,
            highlightColor: String
        ) {
            if (chosenCityInterface.getChosenCityID() == itemID) {
                cityTV.setTextColor(Color.parseColor(highlightColor))
                temperatureTV.setTextColor(Color.parseColor(highlightColor))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener, this, highlightColor)
    }

    override fun changeChosenInBase(newChosenID: String) {
        chosenID = newChosenID
    }

    override fun getChosenCityID(): String {
        return chosenID
    }

    companion object {
        val diffUtilCallback: DiffUtil.ItemCallback<CityWeather>
            get() = object : DiffUtil.ItemCallback<CityWeather>() {
                override fun areItemsTheSame(oldItem: CityWeather, newItem: CityWeather): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: CityWeather, newItem: CityWeather): Boolean {
                    return oldItem == newItem
                }
            }
    }
}