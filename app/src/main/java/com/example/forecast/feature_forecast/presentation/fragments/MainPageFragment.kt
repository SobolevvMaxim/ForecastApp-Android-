package com.example.forecast.feature_forecast.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.trimmedLength
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.di.DateFormat
import com.example.forecast.di.TimeFormat
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.CitiesViewModel
import com.example.forecast.feature_forecast.presentation.adapters.DayForecastAdapter
import com.example.forecast.feature_forecast.presentation.adapters.WeekForecastAdapter
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
class MainPageFragment : Fragment(R.layout.main_page_fragment) {
    companion object {
        fun create() = MainPageFragment()
    }

    @Inject
    @DateFormat
    lateinit var mainDateFormat: SimpleDateFormat

    @Inject
    @TimeFormat
    lateinit var mainTimeFormat: SimpleDateFormat

    private val viewModel by viewModels<CitiesViewModel>({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isConnected())
            onChangeNetworkState(false)

        setupListeners()

        viewModel.getAddedCities(post = false) // at start if you search a forecast repository data is empty

        val chosenCityID = (activity as ChosenCityInterface).getChosenCityID()

        if (savedInstanceState == null) {
            viewModel.getCityByID(cityID = chosenCityID)
        }

        viewModel.chosenLiveData.observe(viewLifecycleOwner, cityObserver)
        viewModel.errorLiveData.observe(viewLifecycleOwner, errorObserver)
    }

    private val cityObserver = Observer<CityWeather?> { city ->
        swipe_layout.isRefreshing = false
        city?.let {
            Log.d(getString(R.string.main_log), "Observe city: $it")
            checkToUpdate(it)
            updateView(it)
            (activity as ChosenCityInterface).changeChosenInBase(it.id)
        } ?: run {
            updateProgressBar(true)
            viewModel.searchCityForecastByName(getString(R.string.default_city))
        }
    }

    private val errorObserver = Observer<String> {

        Log.d(getString(R.string.main_log), "Observe error:$it")
        Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_LONG).show()

    }

    private fun onRefreshListener() {
        currentCity.text?.let {
            if (!networkCheckByUI()) {
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

    private fun setupListeners() {
        setNetworkListener(context)

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

    private fun checkToUpdate(city: CityWeather) {
        val cityDate = Calendar.getInstance()
        val currentDate = Calendar.getInstance()

        cityDate.apply {
            time = getCityForecastDate(city)
            add(Calendar.HOUR, 1)
        }

        if (cityDate.time.before(currentDate.time) && networkCheckByUI()) {
            Log.d(getString(R.string.main_log), "AutoUpdating city: $city")
            updateCityForecast(city)
        }
    }

    private fun updateCityForecast(city: CityWeather) {
        viewModel.updateCityForecast(city)
        updateProgressBar(true)
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
                updateProgressBar(true)
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
                return when (networkCheckByUI()) {
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

    private fun updateProgressBar(visible: Boolean) {
        when (visible) {
            true -> loading_city_progress.visibility = View.VISIBLE
            false -> loading_city_progress.visibility = View.INVISIBLE
        }
    }

    private fun showCitiesFragment() {
        Log.d(getString(R.string.main_log), "Showing cities fragment...")
        (activity as NavigationHost).navigateToCitiesFragment()
    }

    private fun updateView(city: CityWeather) {
        updateProgressBar(false)
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
            sunrise_value.text = getTime(city.sunrise)
            sunset_value.text = getTime(city.sunset)
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

    private fun setNetworkListener(context: Context?) {
        val manager: ConnectivityManager =
            context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val builder = NetworkRequest.Builder()

            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

            val networkRequest = builder.build()
            manager.registerNetworkCallback(networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        onChangeNetworkState(true)
                        Log.d(getString(R.string.main_log), "Network available!")
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        onChangeNetworkState(false)
                        Log.d(getString(R.string.main_log), "Network unavailable!")
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        onChangeNetworkState(false)
                        Log.d(getString(R.string.main_log), "Network lost!")
                    }
                })

        }
    }

    private fun isConnected(): Boolean {
        try {
            val command = "ping -c 1 google.com"
            return Runtime.getRuntime().exec(command).waitFor() == 0
        } catch (e: Exception) {

        }
        return false
    }

    fun onChangeNetworkState(available: Boolean) {
        activity?.runOnUiThread {
            when (available) {
                true -> offline_mode.visibility = View.GONE
                false -> offline_mode.visibility = View.VISIBLE
            }
        }
    }

    private fun networkCheckByUI(): Boolean = !offline_mode.isVisible

    private fun getCityForecastDate(city: CityWeather) =
        mainDateFormat.parse(city.forecastDate) ?: Date(1)

    private fun getTime(time: String): String = mainTimeFormat.format(Date(time.toLong()))
}