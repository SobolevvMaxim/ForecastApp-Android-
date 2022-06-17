package com.example.forecast.feature_forecast.presentation.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.extensions.DateUtils.checkIfDeprecated
import com.example.extensions.DateUtils.getTime
import com.example.extensions.LocationUtils.getLastLocation
import com.example.extensions.LocationUtils.getLocationPermissions
import com.example.extensions.NetworkManager
import com.example.extensions.NetworkUtils.isOnline
import com.example.extensions.UIUtils.addMargins
import com.example.extensions.UIUtils.closeNavigationViewOnBackPressed
import com.example.extensions.UIUtils.getNavigationBarHeight
import com.example.extensions.UIUtils.getStatusBarHeight
import com.example.extensions.UIUtils.networkCheckByUI
import com.example.extensions.UIUtils.updateProgressBar
import com.example.features.RecyclerClickListener
import com.example.forecast.R
import com.example.forecast.di.DateFormat
import com.example.forecast.di.TimeFormat
import com.example.forecast.domain.data_processing.DataProcessing
import com.example.forecast.domain.data_processing.TemperatureUnit
import com.example.forecast.domain.model.CityToSearch
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.model.Coordinates
import com.example.forecast.feature_forecast.base.BaseFragment
import com.example.forecast.feature_forecast.base.Event
import com.example.forecast.feature_forecast.presentation.adapters.CitiesRecyclerAdapter
import com.example.forecast.feature_forecast.presentation.adapters.DayForecastAdapter
import com.example.forecast.feature_forecast.presentation.adapters.WeekForecastAdapter
import com.example.forecast.feature_forecast.presentation.viewmodels.CitiesViewModel
import com.example.forecast.feature_forecast.presentation.viewmodels.MainViewModel
import com.example.forecast.feature_forecast.utils.ChosenCityInterface
import com.example.forecast.feature_forecast.utils.Utils.checkCityInput
import com.example.forecast.feature_forecast.utils.Utils.getForecastImageID
import com.example.forecast.feature_settings.SettingsActivity
import com.example.forecast.feature_settings.SettingsPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_city_dialog.*
import kotlinx.android.synthetic.main.additional_forecast_info.*
import kotlinx.android.synthetic.main.cities_header.*
import kotlinx.android.synthetic.main.main_app_bar.*
import kotlinx.android.synthetic.main.main_forecast_info.*
import kotlinx.android.synthetic.main.main_navigation.*
import kotlinx.android.synthetic.main.main_page_fragment.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainPageFragment : BaseFragment<MainViewModel>(res = R.layout.main_page_fragment) {

    private val mainDrawer by lazy {
        fragment_drawer
    }

    @Inject
    @DateFormat
    lateinit var mainDateFormat: SimpleDateFormat

    @Inject
    @TimeFormat
    lateinit var mainTimeFormat: SimpleDateFormat

    override val viewModel by viewModels<MainViewModel>()

    private val citiesViewModel by viewModels<CitiesViewModel>()

    private val cityObserver = Observer<Event<CityWeather>> { city ->
        when (city) {
            is Event.Loading -> onLoading()
            is Event.Success<CityWeather> -> onSuccess(city.data)
            is Event.Error -> onError(city.throwable)
        }
    }

    private val settingsPreferences by lazy {
        SettingsPreferences(
            PreferenceManager(requireContext()).sharedPreferences,
            requireContext()
        )
    }

    private val _networkManager by lazy { NetworkManager(context, ::onChangeNetworkState) }

    private fun onChangeNetworkState(available: Boolean) {
        activity?.runOnUiThread {
            when (available) {
                true -> offline_mode.visibility = View.GONE
                false -> offline_mode.visibility = View.VISIBLE
            }
        }
    }

    private val citiesObserver = Observer<Set<CityWeather>> { cities ->
        cities.run {
            Timber.d("Observed cities: %s", cities)
            if (this.isNullOrEmpty()) {
                Timber.d("Getting last location...")
                getLastLocation(
                    successCallback = {
                        Timber.d(
                            "Location success callback (lat: %s, lon: %s)",
                            it.latitude,
                            it.longitude
                        )
                        viewModel.searchForecastByCoordinates(
                            cityToSearch = CityToSearch(
                                coordinates = Coordinates(
                                    lat = it.latitude.toString(),
                                    lon = it.longitude.toString()
                                ),
                                searchName = getString(R.string.location_title)
                            )
                        )
                    },
                    locationNullCallback = {
                        Timber.d("Location null callback")
                        searchDefaultCityForecast()
                    },
                    noPermissionCallback = {
                        Timber.d("No location permission gained")
                        searchDefaultCityForecast()
                    }
                )
                return@Observer
            }

            updateRecyclerView(cities)
        }
    }

    private val chosenObserver = Observer<String> { newChosenID ->
        Timber.d("Observed chosen: %s", newChosenID)
        viewModel.getCityByID(newChosenID)
        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenCityID(newChosenID)
    }

    private val citiesRecyclerAdapter by lazy {
        CitiesRecyclerAdapter(
            listener = RecyclerClickListener(
                clickListener = {
                    Timber.d("Cities recycler clicked on item: %s", it)
                    changeChosen(it.id)
                    mainDrawer.closeDrawers()
                },
                onLongClickListener = {
                    Timber.d("Cities recycler long clicked on item: %s", it)
                    deleteCityDialog(it)
                }
            ),
            chosenID = getString(R.string.default_chosen_id),
            highlightColor = ContextCompat.getColor(requireContext(), R.color.primaryColor),
            commonColor = ContextCompat.getColor(requireContext(), R.color.black),
            unit = settingsPreferences.getTemperatureUnit(),
        )
    }

    private fun searchDefaultCityForecast() {
        Timber.d("Searching default city forecast...")
        viewModel.searchCityInfoByName(
            cityToSearch = CityToSearch(searchName = getString(R.string.default_city))
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changeChosen(newChosenID: String) {
        Timber.d("Changing chosen to %s", newChosenID)
        viewModel.changeChosenInBase(newChosenID)
        citiesRecyclerAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onResume() {
        super.onResume()

        closeNavigationViewOnBackPressed(drawerLayout = mainDrawer)
        _networkManager.registerNetworkListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isOnline())
            offline_mode.visibility = View.GONE

        getLocationPermissions(
            onPermissionGained = {
                Timber.d("Location permission gained...")
                citiesViewModel.getAddedCities()
            },
            onPermissionDialogClosed = {
                // TODO: callback triggered sometimes when permission dialog is still opened
            }
        )


        activity?.window?.setBackgroundDrawableResource(R.drawable.back)

        setupListeners()
        setHiddenStatusBarMargins()

        viewModel.chosenLiveData.observe(viewLifecycleOwner, cityObserver)
        viewModel.chosenID.observe(viewLifecycleOwner, chosenObserver)
        citiesViewModel.citiesLiveData.observe(viewLifecycleOwner, citiesObserver)

        setRecyclerView()
    }

    override fun onPause() {
        super.onPause()

        _networkManager.unregisterNetworkListener()
    }

    private fun onSuccess(city: CityWeather) {
        Timber.d("Observed city success: %s", city)
        loading_city_progress.updateProgressBar(false)
        swipe_layout.isRefreshing = false
        city.run {
            checkIfDeprecated(
                dateFormat = mainDateFormat,
                deprecatedAction = { deprecatedCityForecast ->
                    if (offline_mode.networkCheckByUI())
                        viewModel.updateCityForecast(deprecatedCityForecast)
                },
                settingsPreferences.getAutoUpdateTime()
            )
            updateView(this)
            citiesViewModel.getAddedCities()
            viewModel.changeChosenInBase(id)
            changeChosen(id)
        }
    }

    override fun onLoading() {
        Timber.d("On loading...")
        loading_city_progress.updateProgressBar(true)
    }

    private fun setupListeners() {
        mainDrawer.useCustomBehavior(Gravity.LEFT)

        swipe_layout.setOnRefreshListener {
            onRefreshListener()
        }

        topAppBar.apply {
            setNavigationOnClickListener {
                mainDrawer.openDrawer(Gravity.LEFT)
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.add_button -> {
                        addCityDialog()
                        true
                    }
                    else -> false
                }
            }
        }

        settings_button.setOnClickListener {
            startActivity(Intent(context, SettingsActivity::class.java))
        }
    }

    private fun setHiddenStatusBarMargins() {
        val statusBarHeight: Int = getStatusBarHeight()
        fragment_drawer.addMargins(0, 0, 0, getNavigationBarHeight())
        main_fragment_parent.addMargins(0, statusBarHeight, 0, 0)
        cities_navigation.addMargins(0, statusBarHeight, 0, 0)
    }

    private fun setRecyclerView() {
        val recyclerManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        cities_recycler.apply {
            layoutManager = recyclerManager
        }

        cities_recycler.adapter = citiesRecyclerAdapter
    }

    private fun updateRecyclerView(cities: Set<CityWeather>) {
        citiesRecyclerAdapter.submitList(cities.toList())
    }

    private fun onRefreshListener() {
        Timber.d("Refresh layout triggered...")
        if (!offline_mode.networkCheckByUI()) {
            Toast.makeText(
                context,
                getString(R.string.network_unavailable),
                Toast.LENGTH_SHORT
            ).show()
            swipe_layout.isRefreshing = false
            return
        }

        viewModel.run {
            updateCityForecast((chosenLiveData.value as Event.Success<CityWeather>).data)
        }
    }

    @SuppressLint("InflateParams")
    private fun addCityDialog() {
        AlertDialog.Builder(requireContext(), R.style.AlertDialog_Forecast).create().apply {
            val inflater = requireActivity().layoutInflater
            setView(inflater.inflate(R.layout.add_city_dialog, null))
            setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.positive_button)) { _, _ ->
                val cityInput = city_edit_text.text.toString().trim()

                if (!checkCityInput(cityInput))
                    return@setButton

                viewModel.searchCityInfoByName(CityToSearch(searchName = cityInput))
            }
            setButton(
                AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.negative_button)
            ) { dialog, _ ->
                dialog.cancel()
            }
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)
            show()
        }
    }

    private fun deleteCityDialog(city: CityWeather) {
        AlertDialog.Builder(requireContext()).create().apply {
            val title = "${getString(R.string.delete_city_title)} ${city.name}?"
            setTitle(title)
            setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
                when (city.id == viewModel.chosenID.value) {
                    false -> {
                        citiesViewModel.deleteCity(city)

                    }
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

    private fun updateView(cityToUpdateView: CityWeather) {
        Timber.d("Updating main view (city: %s)", cityToUpdateView)

        val temperatureUnit = settingsPreferences.getTemperatureUnit()
        DataProcessing(
            cityToUpdateView,
            temperatureUnit
        ).apply {
            if (cityToUpdateView.name == getString(R.string.location_title))
                your_location.visibility = View.VISIBLE
            else your_location.visibility = View.INVISIBLE

            currentCity.text = getForecastLocation()
            temperature_today.text = getTemperature()
            uvindex_value.text = getUVI()
            feels_like_view.text = getFeelsLike(getString(R.string.feels_like_title))
            currentDate.text = getForecastDate()
            humidity_value.text = getHumidity()
            setForecastImage(city)
            sunrise_value.text = mainTimeFormat.getTime(city.sunrise)
            sunset_value.text = mainTimeFormat.getTime(city.sunset)

            setDailyRecyclerView(city, temperatureUnit)
            setHourlyRecyclerView(city, temperatureUnit)
        }
    }

    private fun setForecastImage(city: CityWeather) {
        big_image.setImageResource(city.dailyTemperatures[0].description.getForecastImageID())
    }

    private fun setDailyRecyclerView(city: CityWeather, temperatureUnit: TemperatureUnit) {
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
                resources.getStringArray(R.array.days).toList(),
                temperatureUnit
            )
        daily_forecast_recycler.adapter = forecastAdapter
    }

    private fun setHourlyRecyclerView(city: CityWeather, temperatureUnit: TemperatureUnit) {
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
                temperatureUnit
            )
        hourly_forecast_recycler.adapter = forecastAdapter
    }
}