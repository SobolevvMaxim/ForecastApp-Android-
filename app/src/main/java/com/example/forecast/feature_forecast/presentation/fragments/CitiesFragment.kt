package com.example.forecast.feature_forecast.presentation.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.text.trimmedLength
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.checkNetwork
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.*
import com.example.forecast.feature_forecast.presentation.adapters.CitiesRecyclerAdapter
import com.example.forecast.feature_forecast.presentation.adapters.RecyclerOnCLickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_city_dialog.*
import kotlinx.android.synthetic.main.choose_city_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CitiesFragment : Fragment(R.layout.choose_city_fragment), RightSwipeNavigation {
    companion object {
        fun create() = CitiesFragment()
    }

    @Inject
    lateinit var format: SimpleDateFormat

    private val mDetector: GestureDetectorCompat by lazy {
        GestureDetectorCompat(
            requireActivity().applicationContext,
            SwipeListener(rightSwipeNavigation = this)
        )
    }

    private val viewModel by viewModels<CitiesViewModel>({ requireActivity() })

    private val citiesRecyclerAdapter: CitiesRecyclerAdapter = CitiesRecyclerAdapter(
        RecyclerOnCLickListener(
            { city ->
                val cityDate: Date = getCityForecastDate(city)

                if (!DateUtils.isToday(cityDate.time)) {
                    deprecatedForecastDialog(city)
                } else {
                    changeChosenCity(city.id)
                    navigateToMainFragment()
                }
            }, { cityToDelete ->
                deleteCityDialog(city = cityToDelete)
            }),
        "0"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getAddedCities()
        }

        viewModel.citiesLiveData.observe(viewLifecycleOwner) { cities ->
            if (cities.isEmpty())
                addCityDialog()
            updateProgressBar(false)
            updateRecyclerView(cities)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            Log.d("MY_ERROR", "error: $error")
        }

        button_add_city.setOnClickListener {
            addCityDialog()
        }

        app_bar.setNavigationOnClickListener {
            navigateToMainFragment()
        }

        setRecyclerView()
        format
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    @SuppressLint("InflateParams")
    private fun addCityDialog() {
        AlertDialog.Builder(requireContext()).create().apply {
            val inflater = requireActivity().layoutInflater
            setView(inflater.inflate(R.layout.add_city_dialog, null))
            setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
                val cityInput = city_edit_text.text.toString()

                when (cityInput.trimmedLength()) {
                    in 0..3 -> Toast.makeText(
                        requireContext(),
                        "Incorrect input!",
                        Toast.LENGTH_LONG
                    ).show()
                    else -> {
                        viewModel.searchCityForecastByName(cityInput)
                        updateProgressBar(true)
                    }
                }
            }
            setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { dialog, _ ->
                dialog.cancel()
            }
            show()
        }
    }

    private fun deprecatedForecastDialog(city: CityWeather) {
        AlertDialog.Builder(requireContext()).create().apply {
            setTitle(getString(R.string.deprecated_forecast_title))
            setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
                viewModel.updateCityForecast(city)
                updateProgressBar(true)
            }
            setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, _ ->
                dialog.cancel()
                navigateToMainFragment()
            }
            show()
        }
    }

    private fun deleteCityDialog(city: CityWeather) {
        AlertDialog.Builder(requireContext()).create().apply {
            val title = "${getString(R.string.delete_city_title)} ${city.name}?"
            setTitle(title)
            setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
                when (city.id == (activity as ChosenCityInterface).getChosenCityID()) {
                    false -> viewModel.deleteCity(city)
                    true -> Toast.makeText(
                        requireContext(),
                        "Unable to delete chosen city!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, _ ->
                dialog.cancel()
            }
            show()
        }
    }

    private fun navigateToMainFragment() {
        parentFragmentManager.popBackStack()
    }

    private fun setRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val userRecycle: RecyclerView = cities_recyclerView
        userRecycle.layoutManager = layoutManager
        userRecycle.setOnTouchListener { _, p1 ->
            app_bar.performClick()
            mDetector.onTouchEvent(p1)
        }
        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenInBase((activity as ChosenCityInterface).getChosenCityID())

        userRecycle.adapter = citiesRecyclerAdapter
    }

    private fun changeChosenCity(id: String) {
        (activity as ChosenCityInterface).changeChosenInBase(id)
        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenInBase(id)
    }

    private fun updateRecyclerView(cities: Set<CityWeather>) {
        citiesRecyclerAdapter.submitList(cities.toList())
    }

    private fun updateProgressBar(visible: Boolean) {
        if (visible)
            loading_city_progress.visibility = View.VISIBLE
        else loading_city_progress.visibility = View.GONE
    }

    private fun getCityForecastDate(city: CityWeather) = format.parse(city.forecastDate) ?: Date(1)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

        if (!checkNetwork(context)) offline_mode_cities.visibility =
            View.GONE else offline_mode_cities.visibility = View.VISIBLE
    }

    override fun onRightSwipe() {
        navigateToMainFragment()
    }
}