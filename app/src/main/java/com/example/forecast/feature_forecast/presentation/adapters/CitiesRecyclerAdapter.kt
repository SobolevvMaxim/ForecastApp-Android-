package com.example.forecast.feature_forecast.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.features.RecyclerClickListener
import com.example.forecast.R
import com.example.forecast.domain.data_processing.DataProcessing
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.utils.ChosenCityInterface
import kotlinx.android.synthetic.main.city_item.view.*

class CitiesRecyclerAdapter(
    private val listener: RecyclerClickListener<CityWeather>,
    private var chosenID: String,
    private val highlightColor: Int,
    private val commonColor: Int,
) :
    ListAdapter<CityWeather, CitiesRecyclerAdapter.ViewHolder>(diffUtilCallback),
    ChosenCityInterface {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cityTV: TextView = view.item_city
        private val temperatureTV: TextView = view.city_temperature
        private val itemLocation: ImageView = view.item_location

        fun bind(
            item: CityWeather,
            listener: RecyclerClickListener<CityWeather>,
            chosenCityInterface: ChosenCityInterface,
            highlightColor: Int,
            commonColor: Int
        ) = with(itemView) {
            DataProcessing(item).apply {
                cityTV.text = getForecastLocation()
                temperatureTV.text = getTemperature()
            }

            highlightIfChosenCity(
                itemID = item.id,
                chosenCityInterface,
                highlightColor,
                commonColor
            )

            showLocation(item.name)

            setOnClickListener {
                listener.clickListener?.invoke(item)
            }
            setOnLongClickListener {
                listener.onLongClickListener?.invoke(item)
                true
            }
        }

        private fun highlightIfChosenCity(
            itemID: String,
            chosenCityInterface: ChosenCityInterface,
            highlightColor: Int,
            commonColor: Int
        ) {
            if (chosenCityInterface.getChosenCityID() == itemID) {
                cityTV.setTextColor(highlightColor)
            } else {
                cityTV.setTextColor(commonColor)
            }
        }

        private fun showLocation(itemName: String) {
            if (itemName == "Your Location") {
                itemLocation.visibility = View.VISIBLE
            } else {
                itemLocation.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener, this, highlightColor, commonColor)
    }

    override fun changeChosenCityID(newChosenID: String) {
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

                override fun areContentsTheSame(
                    oldItem: CityWeather,
                    newItem: CityWeather
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}