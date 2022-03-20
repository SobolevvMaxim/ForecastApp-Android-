package com.example.forecast.feature_forecast.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.features.LeftSwipeNavigation
import com.example.features.SwipeListener
import com.example.forecast.R
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.presentation.CitiesViewModel
import com.example.forecast.feature_forecast.presentation.adapters.CitiesRecyclerAdapter
import com.example.forecast.feature_forecast.presentation.adapters.RecyclerOnCLickListener
import com.example.forecast.feature_forecast.presentation.base.BaseFragment
import com.example.forecast.feature_forecast.presentation.base.Event
import com.example.forecast.feature_forecast.presentation.utils.ChosenCityInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.choose_city_fragment.*

@AndroidEntryPoint
class CitiesFragment : BaseFragment<CitiesViewModel>(), LeftSwipeNavigation {
    companion object {
        fun create() = CitiesFragment()
    }

    private val mDetector: GestureDetectorCompat by lazy {
        GestureDetectorCompat(
            requireActivity().applicationContext,
            SwipeListener(leftSwipeNavigation = this)
        )
    }

    override val viewModel by viewModels<CitiesViewModel>({ requireActivity() })

    private val citiesRecyclerAdapter: CitiesRecyclerAdapter = CitiesRecyclerAdapter(
        RecyclerOnCLickListener(
            clickListener = { city ->
                changeChosenCity(city.id)
                navigateToMainFragment()
            },
            onLongClickListener = { cityToDelete ->
                deleteCityDialog(city = cityToDelete)
            }),
        chosenID = "0",
        highlightColor = "#4680C5"
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
            viewModel.getAddedCities(postResults = true)
            Log.d(getString(R.string.main_log), "Loading cities from base...")
        }

        viewModel.citiesLiveData.observe(viewLifecycleOwner) { cities ->
            when (cities) {
                is Event.Loading -> onLoading()
                is Event.Success<Set<CityWeather>> -> cities.data?.let { updateRecyclerView(it) }
                is Event.Error -> onError(cities.throwable)
            }
        }

        setRecyclerView()
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
        Log.d(getString(R.string.main_log), "Navigating to Main Fragment...")
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

    private fun changeChosenCity(newChosenID: String) {
        Log.d(
            getString(R.string.main_log),
            "Changing chosen from cities fragment (new chosen id: $newChosenID)"
        )
        viewModel.getCityByID(newChosenID)
        (activity as ChosenCityInterface).changeChosenInBase(newChosenID)
        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenInBase(newChosenID)
    }

    private fun updateRecyclerView(cities: Set<CityWeather>) {
        Log.d(getString(R.string.main_log), "Updating cities recycler: $cities")
        citiesRecyclerAdapter.submitList(cities.toList())
    }

    override fun onLeftSwipe() {
        navigateToMainFragment()
    }
}