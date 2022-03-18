package com.example.forecast.feature_forecast.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.trimmedLength
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.extensions.DateUtils.getCityForecastDate
import com.example.extensions.DateUtils.getTime
import com.example.extensions.NetworkUtils.isOnline
import com.example.extensions.NetworkUtils.onChangeNetworkState
import com.example.extensions.NetworkUtils.setNetworkListener
import com.example.extensions.UIUtils.networkCheckByUI
import com.example.extensions.UIUtils.updateProgressBar
import com.example.forecast.R
import com.example.forecast.di.DateFormat
import com.example.forecast.di.TimeFormat
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.CitiesViewModel
import com.example.forecast.feature_forecast.presentation.adapters.DayForecastAdapter
import com.example.forecast.feature_forecast.presentation.adapters.WeekForecastAdapter
import com.example.forecast.feature_forecast.presentation.base.BaseFragment
import com.example.forecast.feature_forecast.presentation.base.Event
import com.example.forecast.feature_forecast.presentation.utils.ChosenCityInterface
import com.example.forecast.feature_forecast.presentation.utils.NavigationHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_city_dialog.*
import kotlinx.android.synthetic.main.main_page_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class MainPageFragment : BaseFragment<CitiesViewModel>() {
    companion object {
        fun create() = MainPageFragment()
    }

    @Inject
    @DateFormat
    lateinit var mainDateFormat: SimpleDateFormat

    @Inject
    @TimeFormat
    lateinit var mainTimeFormat: SimpleDateFormat

    override val viewModel by viewModels<CitiesViewModel>({ requireActivity() })

    private val cityObserver = Observer<Event<CityWeather>> { city ->
        when (city) {
            is Event.Loading -> onLoading()
            is Event.Success<CityWeather> -> city.data?.let{onSuccess(it)}
            is Event.Error -> city.throwable?.let {
                onError(it)
            } ?:  viewModel.searchCityForecastByName(getString(R.string.default_city))
        }
    }

    private fun onSuccess(city: CityWeather) {
            loading_city_progress.updateProgressBar(false)
            swipe_layout.isRefreshing = false
            checkToUpdate(city)
            updateView(city)
            (activity as ChosenCityInterface).changeChosenInBase(city.id)
    }

    override fun onLoading() {
        loading_city_progress.updateProgressBar(true)
        Log.d(getString(R.string.main_log), "Loading...")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_page_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isOnline())
            onChangeNetworkState(false, offline_mode)

        setupListeners()

        val chosenCityID = (activity as ChosenCityInterface).getChosenCityID()

        if (savedInstanceState == null) {
            viewModel.getCityByID(cityID = chosenCityID)
        }

        viewModel.chosenLiveData.observe(viewLifecycleOwner, cityObserver)
    }

    private fun setupListeners() {
        setNetworkListener(offline_mode)

        menu_button.setOnClickListener {
            showCitiesFragment()
        }

        mainAddButton.setOnClickListener {
            addCityDialog()
        }

        swipe_layout.setOnRefreshListener {
            onRefreshListener()
        }
    }

    private fun onRefreshListener() {
        currentCity.text?.let {
            if (!offline_mode.networkCheckByUI()) {
                Toast.makeText(
                    context,
                    getString(R.string.network_unavailable),
                    Toast.LENGTH_SHORT
                ).show()
                swipe_layout.isRefreshing = false
                return@onRefreshListener
            }
            Log.d(getString(R.string.main_log), "Updating city: $it")
            viewModel.searchCityForecastByName(it.subSequence(0, it.length - 4))
        }
    }

    private fun checkToUpdate(city: CityWeather) {
        val cityDate = Calendar.getInstance()
        val currentDate = Calendar.getInstance()

        cityDate.apply {
            time = mainDateFormat.getCityForecastDate(city)
            add(Calendar.HOUR, 1)
        }

        if (cityDate.time.before(currentDate.time) && offline_mode.networkCheckByUI()) {
            Log.d(getString(R.string.main_log), "AutoUpdating city: $city")
            updateCityForecast(city)
        }
    }

    private fun updateCityForecast(city: CityWeather) {
        viewModel.updateCityForecast(city)
        Log.d(getString(R.string.main_log), "Updating forecast...")
    }

    @SuppressLint("InflateParams")
    private fun addCityDialog() {
        AlertDialog.Builder(requireContext()).create().apply {
            val inflater = requireActivity().layoutInflater
            setView(inflater.inflate(R.layout.add_city_dialog, null))
            setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.positive_button)) { _, _ ->
                val cityInput = city_edit_text.text.toString()

                if (!checkCityInput(cityInput))
                    return@setButton

                Log.d(getString(R.string.main_log), "Searching city: $cityInput")
                viewModel.searchCityForecastByName(cityInput)
            }
            setButton(
                AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.negative_button)
            ) { dialog, _ ->
                dialog.cancel()
            }
            show()
        }
    }

    private fun checkCityInput(cityInput: String): Boolean {
        return when (cityInput.trimmedLength()) {
            in 0..3 -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.incorrect_input),
                    Toast.LENGTH_LONG
                ).show()
                false
            }
            else -> {
                return when (offline_mode.networkCheckByUI()) {
                    true -> {
                        true
                    }
                    false -> {
                        Toast.makeText(
                            context,
                            getString(R.string.network_unavailable),
                            Toast.LENGTH_SHORT
                        ).show()
                        false
                    }
                }
            }
        }
    }

    private fun showCitiesFragment() {
        Log.d(getString(R.string.main_log), "Showing cities fragment...")
        (activity as NavigationHost).navigateToCitiesFragment()
    }

    private fun updateView(city: CityWeather) {
        Log.d(getString(R.string.main_log), "Updating view...")

        city.apply {
            val cityInfoText = "$name, $country"
            currentCity.text = cityInfoText
            dailyTemperatures[0].run {
                setForecastImage(description)
                val temperature = "$temp°"
                temperature_today.text = temperature
            }
            uvindex_value.text = uvi.toString()
            sunrise_value.text = mainTimeFormat.getTime(city.sunrise)
            sunset_value.text = mainTimeFormat.getTime(city.sunset)
            val feelsLikeValue =
                "${getString(R.string.feels_like_title)} ${feels_like.roundToInt() - 273}°"
            feels_like_view.text = feelsLikeValue
            currentDate.text = forecastDate
            val humidityValue = "$humidity%"
            humidity_value.text = humidityValue

            setDailyRecyclerView(this)
            setHourlyRecyclerView(this)
        }
    }

    private fun setForecastImage(description: String) {
        when (description) {
            "Rain" -> big_image.setImageResource(R.drawable.forecast_rain_icon)
            "Snow" -> big_image.setImageResource(R.drawable.forecast_snow_icon)
            "Clear" -> big_image.setImageResource(R.drawable.forecast_sun_icon)
            else -> big_image.setImageResource(R.drawable.forecast_clouds_icon)
        }
    }

    private fun setDailyRecyclerView(city: CityWeather) {
        Log.d(getString(R.string.main_log), "Setting daily recycler...")
        val recyclerManager: RecyclerView.LayoutManager =
            object : LinearLayoutManager(context, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean = false
            }

        daily_forecast_recycler.layoutManager = recyclerManager

        val date: Date = mainDateFormat.parse(city.forecastDate) ?: Date(1)
        val calendar = Calendar.getInstance()

        calendar.time = date
        val todayDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val forecastAdapter =
            WeekForecastAdapter(
                city.dailyTemperatures.subList(1, city.dailyTemperatures.size),
                todayDayOfWeek,
                resources.getStringArray(R.array.days).toList()
            )
        daily_forecast_recycler.adapter = forecastAdapter
    }

    private fun setHourlyRecyclerView(city: CityWeather) {
        Log.d(getString(R.string.main_log), "Setting hourly recycler...")
        val recyclerManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        hourly_forecast_recycler.layoutManager = recyclerManager

        val date: Date = mainDateFormat.parse(city.forecastDate) ?: Date(1)
        val calendar = Calendar.getInstance()

        calendar.time = date
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        val forecastAdapter =
            DayForecastAdapter(
                city.hourlyTemperatures,
                hourOfDay,
            )
        hourly_forecast_recycler.adapter = forecastAdapter
    }
}