package com.example.forecast.feature_forecast.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import com.example.features.RecyclerOnCLickListener
import com.example.forecast.R
import com.example.forecast.di.DateFormat
import com.example.forecast.di.PreferenceTag
import com.example.forecast.di.TimeFormat
import com.example.forecast.domain.data_processing.DataProcessing
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.adapters.CitiesRecyclerAdapter
import com.example.forecast.feature_forecast.presentation.adapters.DayForecastAdapter
import com.example.forecast.feature_forecast.presentation.adapters.WeekForecastAdapter
import com.example.forecast.feature_forecast.presentation.base.BaseFragment
import com.example.forecast.feature_forecast.presentation.base.Event
import com.example.forecast.feature_forecast.presentation.utils.ChosenCityInterface
import com.example.forecast.feature_forecast.presentation.utils.Utils.getForecastImageID
import com.example.forecast.feature_forecast.presentation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.add_city_dialog.*
import kotlinx.android.synthetic.main.additional_forecast_info.*
import kotlinx.android.synthetic.main.main_app_bar.*
import kotlinx.android.synthetic.main.main_forecast_info.*
import kotlinx.android.synthetic.main.main_page_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainPageFragment : BaseFragment<MainViewModel>(res = R.layout.main_page_fragment) {

    private val navMenu by lazy {
        navigation.menu
    }

    private val mainDrawer by lazy {
        fragment_drawer
    }

    @Inject
    @DateFormat
    lateinit var mainDateFormat: SimpleDateFormat

    @Inject
    @TimeFormat
    lateinit var mainTimeFormat: SimpleDateFormat

    @Inject
    @PreferenceTag
    lateinit var preferenceTag: String

    override val viewModel by viewModels<MainViewModel>()

    private val cityObserver = Observer<Event<CityWeather?>> { city ->
        when (city) {
            is Event.Loading -> onLoading()
            is Event.Success<CityWeather?> -> city.data?.let { onSuccess(it) }
                ?: viewModel.searchCityForecastByName(getString(R.string.default_city))
            is Event.Error -> onError(city.throwable)
        }
    }

    // TODO: Implement ManageCitiesFragment (replace container in main activity)
    // TODO: Move logics of cities menu to somewhere
    // TODO: Change passing chosen city logics
    // TODO: Network listener leak
    // TODO: Navigation button is not clickable after navigating from ManageCitiesFragment
    private val citiesObserver = Observer<Set<CityWeather>> { cities ->
        cities.run {
            if (this.isNullOrEmpty())
                return@Observer

            // TODO: pass to recycler
            updateRecyclerView(cities)
//            addCitiesToMenu(this.toList())
        }
    }

    private val citiesRecyclerAdapter = CitiesRecyclerAdapter(
        listener = RecyclerOnCLickListener(
            clickListener = {
                changeChosen(it.id)
                mainDrawer.close()
            },
            onLongClickListener = {
                deleteCityDialog(it)
            }
        ),
        chosenID = "0",
        highlightColor = "#4680C5"
    )

    private fun deleteCityDialog(city: CityWeather) {
        AlertDialog.Builder(requireContext()).create().apply {
            val title = "${getString(R.string.delete_city_title)} ${city.name}?"
            setTitle(title)
            setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
                when (city.id == (activity as ChosenCityInterface).getChosenCityID()) {
                    false -> {
                        viewModel.deleteCity(city)
                        Log.d(getString(R.string.main_log), "Deleting city: $city")
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

    private fun changeChosen(newChosenID: String) {
        (activity as ChosenCityInterface).changeChosenInBase(newChosenID)
        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenInBase(newChosenID)
        citiesRecyclerAdapter.notifyDataSetChanged()
    }

    private val sharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, p1 ->
            Log.d(
                getString(R.string.main_log),
                "onSharedPreferenceChanged: $p1"
            )
            if (p1 == preferenceTag)
                viewModel.getCityByID((activity as ChosenCityInterface).getChosenCityID())
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).setSupportActionBar(topAppBar)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val prefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
        prefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)

//        requireActivity()
//            .onBackPressedDispatcher
//            .addCallback {
//                if (mainDrawer.isDrawerOpen(GravityCompat.START)) {
//                    mainDrawer.closeDrawer(GravityCompat.START)
//                }
//            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        navigation.setupWithNavController(findNavController())
//        topAppBar.setupWithNavController(findNavController(), mainDrawer)
        if (!isOnline())
            onChangeNetworkState(false, offline_mode)

        setupListeners()

        val chosenCityID = (activity as ChosenCityInterface).getChosenCityID()
        viewModel.getCityByID(cityID = chosenCityID)

        viewModel.chosenLiveData.observe(viewLifecycleOwner, cityObserver)
        viewModel.citiesLiveData.observe(viewLifecycleOwner, citiesObserver)

        setRecyclerView()
    }

    private fun onSuccess(city: CityWeather) {
        loading_city_progress.updateProgressBar(false)
        swipe_layout.isRefreshing = false
        checkToUpdate(city)
        updateView(city)
        viewModel.getAddedCities()
        (activity as ChosenCityInterface).changeChosenInBase(city.id)
    }

    override fun onLoading() {
        loading_city_progress.updateProgressBar(true)
        Log.d(getString(R.string.main_log), "Loading...")
    }

    override fun onPause() {
        super.onPause()
        val prefs = requireActivity().getPreferences(Context.MODE_PRIVATE)
        prefs.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }

    private fun setupListeners() {
        setNetworkListener(offline_mode)

        swipe_layout.setOnRefreshListener {
            onRefreshListener()
        }

        topAppBar.apply {
            setNavigationOnClickListener {
                mainDrawer.open()
                viewModel.getAddedCities()
            }
            Log.d(getString(R.string.main_log), "Navigation listener set")

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.add_button -> {
                        addCityDialog()
                        true
                    }
                    android.R.id.home -> {
                        mainDrawer.open()
                        true
                    }
                    else -> false
                }
            }
        }

        navigation.setNavigationItemSelectedListener { tappedItem ->
//            changeMenuChecked(tappedItem)

//            viewModel.citiesLiveData.value?.firstOrNull { it.name == tappedItem.title }?.let {
//                (activity as ChosenCityInterface).changeChosenInBase(it.id)
//            }

//            findNavController().run {
//                if (currentDestination?.id != R.id.mainPageFragment)
//                    popBackStack()
//            }
            mainDrawer.close()
            true
        }
    }

    private fun setRecyclerView() {
        Log.d(getString(R.string.main_log), "Setting cities recycler...")
        val recyclerManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        cities_recycler.apply {
            layoutManager = recyclerManager
        }
        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenInBase(
            (activity as ChosenCityInterface).getChosenCityID()
        )

        cities_recycler.adapter = citiesRecyclerAdapter
    }

    private fun updateRecyclerView(cities: Set<CityWeather>) {
        Log.d(getString(R.string.main_log), "Updating cities recycler: $cities")
        citiesRecyclerAdapter.submitList(cities.toList())
    }

//    private fun changeMenuChecked(newChecked: MenuItem) {
//        navMenu.forEach { item -> item.isChecked = false }
//        newChecked.isChecked = true
//    }

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
                val cityInput = city_edit_text.text.toString().trim()

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

//    private fun addCitiesToMenu(cities: List<CityWeather>) {
//        val currentChosenID = (activity as ChosenCityInterface).getChosenCityID()
//        navMenu.clear()
//        cities.forEach {
//            val item = navMenu.add(R.id.cities, it.id.toInt(), Menu.NONE, it.name)
//
//            if (it.id == currentChosenID) item.isChecked = true
//        }
//        navigationOptions()
//    }

//    private fun navigationOptions() {
//        navMenu.add(getString(R.string.manage_cities)).setOnMenuItemClickListener {
//            findNavController().run {
//                if (currentDestination?.id != R.id.manageCitiesFragment)
//                    navigate(R.id.action_mainPageFragment_to_manageCitiesFragment)
//            }
//            mainDrawer.close()
//            true
//        }
//        navMenu.add("TODO").setOnMenuItemClickListener {
//            Toast.makeText(context, "TODO pressed!", Toast.LENGTH_SHORT).show()
//            true
//        }
//    }

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

    private fun updateView(cityToUpdateView: CityWeather) {
        Log.d(getString(R.string.main_log), "Updating view...")

        DataProcessing(cityToUpdateView).apply {
            currentCity.text = getForecastLocation()
            temperature_today.text = getTemperature()
            uvindex_value.text = getUVI()
            feels_like_view.text = getFeelsLike(getString(R.string.feels_like_title))
            currentDate.text = getForecastDate()
            humidity_value.text = getHumidity()
            setForecastImage(city)
            sunrise_value.text = mainTimeFormat.getTime(city.sunrise)
            sunset_value.text = mainTimeFormat.getTime(city.sunset)

            setDailyRecyclerView(city)
            setHourlyRecyclerView(city)
        }
    }

    private fun setForecastImage(city: CityWeather) {
        big_image.setImageResource(city.dailyTemperatures[0].description.getForecastImageID())
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