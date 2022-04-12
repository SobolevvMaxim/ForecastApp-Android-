package com.example.forecast.feature_forecast.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
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
import com.example.forecast.R
import com.example.forecast.di.DateFormat
import com.example.forecast.di.TimeFormat
import com.example.forecast.domain.data_processing.DataProcessing
import com.example.forecast.domain.model.CityWeather
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
import kotlinx.android.synthetic.main.main_forecast_info.*
import kotlinx.android.synthetic.main.main_page_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainPageFragment : BaseFragment<MainViewModel>() {

    @Inject
    @DateFormat
    lateinit var mainDateFormat: SimpleDateFormat

    @Inject
    @TimeFormat
    lateinit var mainTimeFormat: SimpleDateFormat

    override val viewModel by viewModels<MainViewModel>()

    private val cityObserver = Observer<Event<CityWeather?>> { city ->
        when (city) {
            is Event.Loading -> onLoading()
            is Event.Success<CityWeather?> -> city.data?.let { onSuccess(it) }
                ?: viewModel.searchCityForecastByName(getString(R.string.default_city))
            is Event.Error -> onError(city.throwable)
        }
    }

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { p0, p1 ->
        Log.d(
            getString(R.string.main_log),
            "onSharedPreferenceChanged: $p1"
        )
        if (p1 == "CHOSEN_CITY")
            viewModel.getCityByID((activity as ChosenCityInterface).getChosenCityID())
    }

//    private val citiesRecyclerAdapter: CitiesRecyclerAdapter = CitiesRecyclerAdapter(
//        RecyclerOnCLickListener(
//            clickListener = { city ->
//                changeChosenCity(city.id)
//                navigateToMainFragment()
//            },
//            onLongClickListener = { cityToDelete ->
//                deleteCityDialog(city = cityToDelete)
//            }),
//        chosenID = "0",
//        highlightColor = "#4680C5"
//    )

    private fun onSuccess(city: CityWeather) {
        loading_city_progress.updateProgressBar(false)
        swipe_layout.isRefreshing = false
        checkToUpdate(city)
        updateView(city)
        (activity as ChosenCityInterface).changeChosenInBase(city.id)
//        loadCitiesToRecycler()
    }

    override fun onLoading() {
        loading_city_progress.updateProgressBar(true)
        Log.d(getString(R.string.main_log), "Loading...")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//
//        // TODO: no fragment drawer??, one live data,
//        val toggle = ActionBarDrawerToggle(requireActivity(), fragment_drawer, topAppBar, R.string.negative_button, R.string.positive_button)
//        fragment_drawer.addDrawerListener(toggle)
//        toggle.syncState()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = ""
        return inflater.inflate(R.layout.main_page_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_button -> {
                addCityDialog()
                true
            }
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isOnline())
            onChangeNetworkState(false, offline_mode)

        setupListeners()

        val chosenCityID = (activity as ChosenCityInterface).getChosenCityID()
        viewModel.getCityByID(cityID = chosenCityID)

        viewModel.chosenLiveData.observe(viewLifecycleOwner, cityObserver)



//        setRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        val prefs = requireActivity().getSharedPreferences("TEST", Context.MODE_PRIVATE)
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        val prefs = requireActivity().getSharedPreferences("TEST", Context.MODE_PRIVATE)
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }

    private fun setupListeners() {
        setNetworkListener(offline_mode)

//        topAppBar.setNavigationOnClickListener {
//            showCitiesFragment()
//        }
//
//        topAppBar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.add_button -> {
//                    addCityDialog()
//                    true
//                }
//                else -> false
//            }
//        }

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

//    private fun deleteCityDialog(city: CityWeather) {
//        AlertDialog.Builder(requireContext()).create().apply {
//            val title = "${getString(R.string.delete_city_title)} ${city.name}?"
//            setTitle(title)
//            setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
//                when (city.id == (activity as ChosenCityInterface).getChosenCityID()) {
//                    false -> {
//                        viewModel.deleteCity(city)
//                        Log.d(getString(R.string.main_log), "Deleting city: $city")
//                    }
//                    true -> Toast.makeText(
//                        requireContext(),
//                        "Unable to delete chosen city!",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//            setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, _ ->
//                dialog.cancel()
//            }
//            show()
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

//    private fun loadCitiesToRecycler() {
//        Log.d(getString(R.string.main_log), "Showing cities fragment...")
////        findNavController().navigate(MainPageFragmentDirections.actionMainPageFragmentToCitiesFragment())
//        viewModel.getAddedCities()
//        Log.d(getString(R.string.main_log), "Loading cities from base...")
//
//        viewModel.citiesLiveData.observe(viewLifecycleOwner) { cities ->
//            when (cities) {
//                is Event.Loading -> onLoading()
//                is Event.Success<Set<CityWeather>> -> cities.data?.let { updateRecyclerView(it) }
//                is Event.Error -> onError(cities.throwable)
//            }
//        }

//        setRecyclerView()
//}

//    private fun changeChosenCity(newChosenID: String) {
//        Log.d(
//            getString(R.string.main_log),
//            "Changing chosen from cities fragment (new chosen id: $newChosenID)"
//        )
//        (activity as ChosenCityInterface).changeChosenInBase(newChosenID)
//        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenInBase(newChosenID)
//    }
//
//    private fun updateRecyclerView(cities: Set<CityWeather>) {
//        Log.d(getString(R.string.main_log), "Updating cities recycler: $cities")
//        citiesRecyclerAdapter.submitList(cities.toList())
//    }

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

//    private fun setRecyclerView() {
//        Log.d(getString(R.string.main_log), "Setting cities recycler...")
//        val recyclerManager: RecyclerView.LayoutManager =
//            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//
//        cities_recyclerView.apply {
//            layoutManager = recyclerManager
//        }
//        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenInBase(
//            (activity as ChosenCityInterface).getChosenCityID()
//        )
//
//        cities_recyclerView.adapter = citiesRecyclerAdapter
//    }

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