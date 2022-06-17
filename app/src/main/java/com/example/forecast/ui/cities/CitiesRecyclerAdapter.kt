package com.example.forecast.ui.cities

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
import com.example.forecast.domain.data_processing.TemperatureUnit
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.ui.main_screen.utils.ChosenCityInterface
import com.example.local.di.DefaultChosenID
import kotlinx.android.synthetic.main.city_item.view.*
import javax.inject.Inject

class CitiesRecyclerAdapter @Inject constructor(
    private val listener: RecyclerClickListener<CityWeather>,
    @DefaultChosenID var chosenID: String = "",
    private val highlightColor: Int,
    private val commonColor: Int,
    private val unit: TemperatureUnit,
) :
    ListAdapter<CityWeather, CitiesRecyclerAdapter.ViewHolder>(diffUtilCallback),
    ChosenCityInterface {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cityTV: TextView = view.item_city
        private val temperatureTV: TextView = view.city_temperature
        private val itemLocation: ImageView = view.item_location

        fun bind(
            item: CityWeather,
        ) = with(itemView) {
            DataProcessing(item, unit).apply {
                cityTV.text = getForecastLocation()
                temperatureTV.text = getTemperature()
            }

            highlightIfChosenCity(itemID = item.id)
            showIconIfLocationItem(item.name)

            setOnClickListener {
                listener.clickListener?.invoke(item)
            }
            setOnLongClickListener {
                listener.onLongClickListener?.invoke(item)
                true
            }
        }

        private fun highlightIfChosenCity(itemID: String) {
            if (chosenID == itemID) {
                cityTV.setTextColor(highlightColor)
            } else {
                cityTV.setTextColor(commonColor)
            }
        }

        private fun showIconIfLocationItem(itemName: String) {
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
        holder.bind(getItem(position))
    }

    override fun changeChosenCityID(newChosenID: String) {
        chosenID = newChosenID
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