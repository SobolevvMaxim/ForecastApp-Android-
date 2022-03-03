package com.example.forecast.feature_forecast.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.R
import com.example.forecast.di.DateFormat
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.CitiesViewModel
import com.example.forecast.feature_forecast.presentation.adapters.CitiesRecyclerAdapter
import com.example.forecast.feature_forecast.presentation.adapters.RecyclerOnCLickListener
import com.example.forecast.feature_forecast.presentation.utils.ChosenCityInterface
import com.example.forecast.feature_forecast.presentation.utils.LeftSwipeNavigation
import com.example.forecast.feature_forecast.presentation.utils.SwipeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.choose_city_fragment.*
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class CitiesFragment : Fragment(), LeftSwipeNavigation {
    companion object {
        fun create() = CitiesFragment()
    }

    @Inject
    @DateFormat
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
                changeChosenCity(city.id)
                navigateToMainFragment()
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
            Log.d(getString(R.string.main_log), "Loading cities from base...")
        }

        viewModel.citiesLiveData.observe(viewLifecycleOwner) { cities ->
            Log.d(getString(R.string.main_log), "Observe cities: $cities")
            updateRecyclerView(cities)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            Log.d(getString(R.string.main_log), "Observe error: $error")
        }

        setRecyclerView()
        format
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

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

    private fun navigateToMainFragment() {
        parentFragmentManager.popBackStack()
    }

    private fun setRecyclerView() {
        Log.d(getString(R.string.main_log), "Setting cities recycler...")
        val recyclerManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        cities_recyclerView.apply {
            layoutManager = recyclerManager
            setOnTouchListener { _, p1 ->
                location_image.performClick()
                mDetector.onTouchEvent(p1)
            }
        }
        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenInBase((activity as ChosenCityInterface).getChosenCityID())

        cities_recyclerView.adapter = citiesRecyclerAdapter
    }

    private fun changeChosenCity(id: String) {
        Log.d(getString(R.string.main_log), "Changing chosen from cities fragment (new chosen id: $id)")
        viewModel.getCityByID(id)
        (activity as ChosenCityInterface).changeChosenInBase(id)
        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenInBase(id)
    }

    private fun updateRecyclerView(cities: Set<CityWeather>) {
        Log.d(getString(R.string.main_log), "Updating cities recycler: $cities")
        citiesRecyclerAdapter.submitList(cities.toList())
    }

    override fun onLeftSwipe() {
        navigateToMainFragment()
    }
}