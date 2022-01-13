package com.example.forecast.feature_forecast.presentation.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.text.trimmedLength
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.forecast.feature_forecast.domain.model.CityWeather
import com.example.forecast.R
import com.example.forecast.feature_forecast.presentation.activities.MainPageActivity
import com.example.forecast.feature_forecast.presentation.adapters.CitiesRecyclerAdapter
import com.example.forecast.feature_forecast.presentation.adapters.RecyclerOnCLickListener
import com.example.forecast.checkNetwork
import com.example.forecast.feature_forecast.presentation.CitiesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.choose_city_fragment.*
import kotlinx.android.synthetic.main.put_city_dialog.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CitiesFragment : Fragment(R.layout.choose_city_fragment) {
    companion object {
        fun create() = CitiesFragment()
    }

    @Inject
    lateinit var format: SimpleDateFormat

    private val citiesRecyclerAdapter: CitiesRecyclerAdapter = CitiesRecyclerAdapter(
        RecyclerOnCLickListener { city ->
            changeChosenCity(newChosenName = city.name)

            val cityDate: Date = format.parse(city.forecastDate) ?: Date(1)

            // TODO: 03.01.2022 forecast deprecated fix
            if (!DateUtils.isToday(cityDate.time)) {
                deprecatedForecastDialog(city)
            } else passCityToMainScreen(city)
        }
    )

    private fun deprecatedForecastDialog(city: CityWeather) {
        AlertDialog.Builder(requireContext()).create().apply {
            setTitle(getString(R.string.deprecated_forecast))
            setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
                // TODO: 13.01.2022
            }
            setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, _ ->
                dialog.cancel()
                passCityToMainScreen(city)
            }
            show()
        }
    }

    private fun passCityToMainScreen(city: CityWeather) {
        val intent = Intent(requireContext(), MainPageActivity::class.java)
        intent.putExtra(getString(R.string.get_city_extra), city)

        startActivity(intent)
    }

    private val viewModel by viewModels<CitiesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.getAddedCities()
        }


        viewModel.citiesLiveData.observe(viewLifecycleOwner) { cities ->
            if (cities.isEmpty())
                showNoticeDialog()
            updateRecyclerView(cities)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            Log.d("MY_ERROR", "error: $error")
        }

        button_add_city.setOnClickListener {
            showNoticeDialog()
        }

        setRecyclerView()
        format
    }

    @SuppressLint("InflateParams")
    private fun showNoticeDialog() {
        AlertDialog.Builder(requireContext()).create().apply {
            val inflater = requireActivity().layoutInflater
            setView(inflater.inflate(R.layout.put_city_dialog, null))
            setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
                val cityInput = city_edit_text.text.toString()

                when (cityInput.trimmedLength()) {
                    in 0..3 -> Toast.makeText(
                        requireContext(),
                        "Incorrect input!",
                        Toast.LENGTH_LONG
                    ).show()
                    else -> viewModel.search(cityInput)
                }
            }
            setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { dialog, _ ->
                dialog.cancel()
            }
            show()
        }
    }

    private fun setRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val userRecycle: RecyclerView = cities_recyclerView
        userRecycle.layoutManager = layoutManager

        userRecycle.adapter = citiesRecyclerAdapter
    }

    private fun changeChosenCity(newChosenName: String) {
        val oldList = citiesRecyclerAdapter.currentList
        val lastChosenIndex = oldList.indexOfLast { it.chosen }
        val newChosenIndex = oldList.indexOfFirst { it.name == newChosenName }

        viewModel.changeChosenCities(lastChosenIndex, newChosenIndex)

        citiesRecyclerAdapter.apply {
            notifyItemChanged(lastChosenIndex)
            notifyItemChanged(newChosenIndex)
        }
    }

    private fun updateRecyclerView(cities: Set<CityWeather>) {
        citiesRecyclerAdapter.submitList(cities.toList())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

        if (!checkNetwork(context)) offline_mode_cities.visibility =
            View.INVISIBLE else offline_mode_cities.visibility = View.VISIBLE
    }
}