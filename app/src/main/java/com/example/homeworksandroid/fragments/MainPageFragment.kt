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
import com.example.homeworksandroid.activities.CitiesActivity
import com.example.homeworksandroid.R
import com.example.homeworksandroid.adapters.ForecastAdapter
import com.example.homeworksandroid.responces.FORMAT
import com.example.homeworksandroid.viewmodels.MainPageViewModel
import kotlinx.android.synthetic.main.main_page_fragment.*

class MainPageFragment : Fragment(R.layout.main_page_fragment) {
    companion object {
        fun create() = MainPageFragment()
    }

    private lateinit var forecastAdapter: ForecastAdapter

    private val viewModel = viewModels<MainPageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.value.citiesLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Log.d("MY_ERROR", "city got in fragment: $it ")
                updateView(it)
            }
        }

        viewModel.value.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
            Log.d("MY_ERROR", "ERROR: $it ")
        }

        view.findViewById<ImageButton>(R.id.mainAddButton).setOnClickListener {
            addCityActivity()
        }
    }

    private fun updateView(city: CityWeather) {
        val cityInfoText = "${city.name}, ${city.country}"
        currentCity.text = cityInfoText
        todaysTemp.text = city.temperatures[0].first.toString()
        today_sunny.text = city.temperatures[0].second
        currentDate.text = city.forecastDate
        setRecyclerView(city)
    }

    private fun setRecyclerView(city: CityWeather) {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val forecastRecycle: RecyclerView? = view?.findViewById(R.id.forecast_recycler)
        forecastRecycle?.layoutManager = layoutManager
        val date = FORMAT.parse(city.forecastDate)!!

        forecastAdapter = ForecastAdapter(city.temperatures.apply { removeFirst() }, date)
        forecastRecycle?.adapter = forecastAdapter
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
        viewModel.value.getCurrentCity()
    }
}