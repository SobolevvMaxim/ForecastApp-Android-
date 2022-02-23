package com.example.forecast.feature_forecast.presentation.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
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
import kotlinx.android.synthetic.main.choose_city_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CitiesFragment : Fragment(), LeftSwipeNavigation {
    companion object {
        fun create() = CitiesFragment()
    }

    @Inject
    lateinit var format: SimpleDateFormat

    private val mDetector: GestureDetectorCompat by lazy {
        GestureDetectorCompat(
            requireActivity().applicationContext,
            SwipeListener(leftSwipeNavigation = this)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.choose_city_fragment, container, false).apply {
            setOnTouchListener { _, p1 ->
                location_image.performClick()
                mDetector.onTouchEvent(p1)
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getAddedCities()
        }

        viewModel.citiesLiveData.observe(viewLifecycleOwner) { cities ->
            updateRecyclerView(cities)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            Log.d("MY_ERROR", "error: $error")
        }

        setRecyclerView()
        format
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    private fun deprecatedForecastDialog(city: CityWeather) {
        AlertDialog.Builder(requireContext()).create().apply {
            setTitle(getString(R.string.deprecated_forecast_title))
            setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
                viewModel.updateCityForecast(city)
                Toast.makeText(context, "Updating forecast...", Toast.LENGTH_SHORT).show()
                navigateToMainFragment()
            }
            setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, _ ->
                dialog.cancel()
                changeChosenCity(city.id)
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setRecyclerView() {
        val recyclerManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        cities_recyclerView.apply {
            layoutManager = recyclerManager
            setOnTouchListener { _, p1 ->
                mDetector.onTouchEvent(p1)
            }
        }
        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenInBase((activity as ChosenCityInterface).getChosenCityID())

        cities_recyclerView.adapter = citiesRecyclerAdapter
    }

    private fun changeChosenCity(id: String) {
        viewModel.getCityByID(id)
        (activity as ChosenCityInterface).changeChosenInBase(id)
        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenInBase(id)
    }

    private fun updateRecyclerView(cities: Set<CityWeather>) {
        citiesRecyclerAdapter.submitList(cities.toList())
    }

    private fun getCityForecastDate(city: CityWeather) = format.parse(city.forecastDate) ?: Date(1)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

        if (!checkNetwork(context)) offline_mode_cities.visibility =
            View.GONE else offline_mode_cities.visibility = View.VISIBLE
    }

    override fun onLeftSwipe() {
        navigateToMainFragment()
    }
}