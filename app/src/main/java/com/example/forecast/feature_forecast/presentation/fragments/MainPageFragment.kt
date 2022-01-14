package com.example.forecast.feature_forecast.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.checkNetwork
import com.example.forecast.feature_forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.NavigationHost
import com.example.forecast.feature_forecast.presentation.activities.P_LOG
import com.example.forecast.feature_forecast.presentation.adapters.WeekForecastAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_page_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainPageFragment : Fragment(R.layout.main_page_fragment) {
    companion object {
        fun create() = MainPageFragment()

        fun create(city: CityWeather, city_tag: String): MainPageFragment {
            val fragment = MainPageFragment()
            val bundle = Bundle()

            bundle.putParcelable(city_tag, city)
            fragment.arguments = bundle

            return fragment
        }
    }

    @Inject
    lateinit var dateFormat: SimpleDateFormat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val cityP: CityWeather? = requireArguments().getParcelable(getString(R.string.get_city_extra))

        cityP?.let {
            Log.d(P_LOG, "onViewCreated: city $cityP")
            updateView(it)
        } ?: (activity as NavigationHost).navigateTo(CitiesFragment.create(), addToBackstack = false)

        mainAddButton.setOnClickListener {
            (activity as NavigationHost).navigateTo(CitiesFragment.create(), false)
        }
        // TODO: 14.01.2022 fix backstack alb
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
        val date: Date = dateFormat.parse(city.forecastDate) ?: Date(1)

        val forecastAdapter = WeekForecastAdapter(city.temperatures.apply { removeFirst() }, date, dateFormat)
        forecast_recycler.adapter = forecastAdapter
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()

        if (!checkNetwork(context)) offline_mode.visibility =
            View.INVISIBLE else offline_mode.visibility = View.VISIBLE
    }
}