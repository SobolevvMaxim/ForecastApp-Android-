package com.example.forecast.ui.cities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.features.RecyclerClickListener
import com.example.forecast.R
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.prefstore.ISettingsPreferences
import com.example.forecast.ui.base.BaseFragment
import com.example.forecast.ui.main_screen.utils.ChosenCityInterface
import com.example.forecast.ui.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.cities_header.*
import kotlinx.android.synthetic.main.fragment_cities.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CitiesFragment : BaseFragment<CitiesViewModel>(R.layout.fragment_cities) {

    @Inject
    lateinit var settingsPreferences: ISettingsPreferences

    override val viewModel by viewModels<CitiesViewModel>()

    private val citiesRecyclerAdapter by lazy {
        CitiesRecyclerAdapter(
            listener = RecyclerClickListener(
                clickListener = {
                    Timber.d("Cities recycler clicked on item: %s", it)
                    changeChosen(it.id)
                },
                onLongClickListener = {
                    Timber.d("Cities recycler long clicked on item: %s", it)
                    deleteCityDialog(it)
                }
            ),
            highlightColor = ContextCompat.getColor(requireContext(), R.color.primaryColor),
            commonColor = ContextCompat.getColor(requireContext(), R.color.black),
            unit = settingsPreferences.getTemperatureUnit(),
        )
    }

    private val chosenObserver by lazy {
        Observer<String> { newChosenID ->
            Timber.d("Observed chosen: %s", newChosenID)
            (citiesRecyclerAdapter as ChosenCityInterface).changeChosenCityID(newChosenID)
            citiesRecyclerAdapter.notifyDataSetChanged()
            viewModel.getAddedCities()
        }
    }

    private val citiesObserver = Observer<Set<CityWeather>> { cities ->
        cities.run {
            Timber.d("Observed cities: %s", cities)
            if (this.isNullOrEmpty()) {
                return@Observer
            }

            updateRecyclerView(cities)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAddedCities()

        viewModel.citiesLiveData.observe(viewLifecycleOwner, citiesObserver)
        viewModel.chosenID.observe(viewLifecycleOwner, chosenObserver)

        settings_button.setOnClickListener {
            startActivity(Intent(context, SettingsActivity::class.java))
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val recyclerManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        cities_recycler.apply {
            layoutManager = recyclerManager
        }

        cities_recycler.adapter = citiesRecyclerAdapter
    }

    private fun deleteCityDialog(city: CityWeather) {
        AlertDialog.Builder(requireContext()).create().apply {
            val title = "${getString(R.string.delete_city_title)} ${city.name}?"
            setTitle(title)
            setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
                when (city.id == viewModel.chosenID.value) {
                    false -> {
                        viewModel.deleteCity(city)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun changeChosen(newChosenID: String) {
        Timber.d("Changing chosen to %s", newChosenID)
        viewModel.changeChosenInBase(newChosenID)
        (citiesRecyclerAdapter as ChosenCityInterface).changeChosenCityID(newChosenID)
        citiesRecyclerAdapter.notifyDataSetChanged()
    }

    private fun updateRecyclerView(cities: Set<CityWeather>) {
        citiesRecyclerAdapter.submitList(cities.toList())
    }
}