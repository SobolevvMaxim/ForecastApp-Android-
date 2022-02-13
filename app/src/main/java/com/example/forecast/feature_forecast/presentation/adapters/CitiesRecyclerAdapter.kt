package com.example.forecast.feature_forecast.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.ChosenCityInterface
import kotlinx.android.synthetic.main.city_item.view.*

class CitiesRecyclerAdapter(
    private val listener: RecyclerOnCLickListener, private var chosenID: String
) :
    ListAdapter<CityWeather, CitiesRecyclerAdapter.ViewHolder>(DiffCallback()),
    ChosenCityInterface {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val city: TextView = view.item_city
        private val temperature: TextView = view.city_temperature

        @SuppressLint("ResourceAsColor")
        fun bind(
            item: CityWeather,
            listener: RecyclerOnCLickListener,
            chosenCityInterface: ChosenCityInterface
        ) = with(itemView) {
            val cityText = "${item.name}, ${item.country}"
            city.text = cityText
            val temperatureText = "${item.temperatures[0].temp}°"
            temperature.text = temperatureText

            highlightIfChosenCity(itemID = item.id, chosenCityInterface)

            setOnClickListener {
                listener.clickListener(item)
            }
            setOnLongClickListener {
                listener.onLongClickListener(item)
                true
            }
        }

        @SuppressLint("ResourceAsColor")
        private fun highlightIfChosenCity(
            itemID: String,
            chosenCityInterface: ChosenCityInterface
        ) {
            if (chosenCityInterface.getChosenCityID() == itemID) {
                city.setTextColor(R.color.primaryColor)
                temperature.setTextColor(R.color.primaryColor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener, this)
    }

    override fun changeChosenInBase(newChosenIndex: String) {
        chosenID = newChosenIndex
    }

    override fun getChosenCityID(): String {
        return chosenID
    }
}

class RecyclerOnCLickListener(
    val clickListener: (newChosenCityName: CityWeather) -> Unit,
    val onLongClickListener: (cityToDelete: CityWeather) -> Unit
)

class DiffCallback : DiffUtil.ItemCallback<CityWeather>() {
    override fun areItemsTheSame(oldItem: CityWeather, newItem: CityWeather): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CityWeather, newItem: CityWeather): Boolean {
        return oldItem == newItem
    }
}