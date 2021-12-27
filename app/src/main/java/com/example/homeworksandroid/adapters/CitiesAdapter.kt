package com.example.homeworksandroid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.R
import kotlinx.android.synthetic.main.city_item.view.*

class CitiesAdapter(private val onClickListener: RecyclerOnCLickListener) :
    ListAdapter<CityWeather, CitiesAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val city: TextView = view.item_city
        private val chosen: ImageView = view.item_done_image
        // private val menu: ImageView = view.dots_menu // todo

        fun bind(item: CityWeather, onClickListener: RecyclerOnCLickListener) = with(itemView) {
            val cityText = "${item.name}, ${item.country}"
            city.text = cityText


            if (item.chosen) chosen.visibility = View.VISIBLE

            setOnClickListener {
                onClickListener.onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.city_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }
}

class RecyclerOnCLickListener(val clickListener: (newChosenCityName: CityWeather) -> Unit) {
    fun onClick(newChosenCityName: CityWeather) = clickListener(newChosenCityName)
}

class DiffCallback : DiffUtil.ItemCallback<CityWeather>() {
    override fun areItemsTheSame(oldItem: CityWeather, newItem: CityWeather): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CityWeather, newItem: CityWeather): Boolean {
        return oldItem == newItem
    }
}