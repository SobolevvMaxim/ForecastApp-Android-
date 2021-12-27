package com.example.homeworksandroid.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homeworksandroid.App
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.FORMAT
import com.example.homeworksandroid.activities.CitiesActivity
import com.example.homeworksandroid.R
import com.example.homeworksandroid.activities.GET_CHOSEN_CITY
import com.example.homeworksandroid.activities.P_LOG
import com.example.homeworksandroid.adapters.ForecastAdapter
import kotlinx.android.synthetic.main.main_page_fragment.*
import java.util.*

class MainPageFragment : Fragment(R.layout.main_page_fragment) {
    companion object {
        fun create() = MainPageFragment()

        fun create(city: CityWeather): MainPageFragment {
            val fragment = MainPageFragment()
            val bundle = Bundle()
            bundle.putParcelable(GET_CHOSEN_CITY, city)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val cityP: CityWeather? = requireArguments().getParcelable(GET_CHOSEN_CITY)

        cityP?.let {
            Log.d(P_LOG, "onViewCreated: city $cityP")
            updateView(cityP)
        } ?: addCityActivity()

        mainAddButton.setOnClickListener {
            addCityActivity()
        }
    }

    private fun updateView(city: CityWeather) {
        city.apply {
            val cityInfoText = "$name, $country"
            currentCity.text = cityInfoText
            temperatures[0].apply {
                todaysTemp.text = temp.toString()
                today_sunny.text = description
            }
            currentDate.text = forecastDate
            setRecyclerView(this)
        }
    }


    private fun setRecyclerView(city: CityWeather) {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        forecast_recycler.layoutManager = layoutManager
        val date: Date = FORMAT.parse(city.forecastDate) ?: Date(1)

        val forecastAdapter = ForecastAdapter(city.temperatures.apply { removeFirst() }, date)
        forecast_recycler.adapter = forecastAdapter
    }

    private fun addCityActivity() {
        startActivity(Intent(context, CitiesActivity::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

        if (!App.checkNetwork(context))
            offline_mode.visibility = View.VISIBLE
        else offline_mode.visibility = View.INVISIBLE
    }
}