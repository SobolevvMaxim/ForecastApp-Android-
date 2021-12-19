package com.example.homeworksandroid.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homeworksandroid.App
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.FORMAT
import com.example.homeworksandroid.activities.CitiesActivity
import com.example.homeworksandroid.R
import com.example.homeworksandroid.adapters.ForecastAdapter
import com.example.homeworksandroid.viewmodels.MainPageViewModel
import kotlinx.android.synthetic.main.main_page_fragment.*
import java.util.*

class MainPageFragment : Fragment(R.layout.main_page_fragment) {
    companion object {
        fun create() = MainPageFragment()
    }

    private val viewModel = viewModels<MainPageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.value.citiesLiveData.observe(viewLifecycleOwner) { city ->
            Log.d("MY_ERROR", "city got in fragment: $city ")
            city?.let {
                updateView(it)
            } ?: addCityActivity()
        }

        viewModel.value.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
            Log.d("MY_ERROR", "ERROR: $it ")
        }

        mainAddButton.setOnClickListener {
            addCityActivity()
        }

        viewModel.value.getCurrentCity()
    }

    private fun updateView(city: CityWeather) {
        city.apply {
            val cityInfoText = "$name, $country"
            currentCity.text = cityInfoText
            temperatures[0].apply {
                todaysTemp.text = first.toString()
                today_sunny.text = second
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