package com.example.forecast.feature_forecast.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.checkNetwork
import com.example.forecast.feature_forecast.data.local.TABLE_NAME
import com.example.forecast.feature_forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.NavigationHost
import com.example.forecast.feature_forecast.presentation.activities.P_LOG
import com.example.forecast.feature_forecast.presentation.adapters.WeekForecastAdapter
import com.example.forecast.feature_forecast.presentation.viewmodels.MainPageViewModel
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

    private val viewModel by viewModels<MainPageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getChosenFromBase()

        viewModel.chosenCityLiveData.observe(viewLifecycleOwner) { chosenCity ->
            updateView(chosenCity)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            if(it.isException()){
                viewModel.searchDefaultForecast(getString(R.string.default_city))
                Log.d(P_LOG, "Error:$it")
                Toast.makeText(requireContext(), "No cities yet!", Toast.LENGTH_LONG).show()
            } else Toast.makeText(requireContext(), "Error in loading default city!", Toast.LENGTH_LONG).show()
        }

//        val cityP: CityWeather? = requireArguments().getParcelable(getString(R.string.get_city_extra))
//
//        cityP?.let {
//            Log.d(P_LOG, "onViewCreated: city $cityP")
//            updateView(it)
//        } ?: (activity as NavigationHost).navigateTo(CitiesFragment.create(), addToBackstack = false)
//
        mainAddButton.setOnClickListener {
            (activity as NavigationHost).navigateTo(CitiesFragment.create(), true)
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
        val date: Date = dateFormat.parse(city.forecastDate) ?: Date(1)

        val forecastAdapter = WeekForecastAdapter(city.temperatures.apply { removeFirst() }, date, dateFormat)
        forecast_recycler.adapter = forecastAdapter
    }

    private fun String.isException(): Boolean = this.startsWith("java.lang")

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()

        if (!checkNetwork(context)) offline_mode.visibility =
            View.INVISIBLE else offline_mode.visibility = View.VISIBLE
    }
}