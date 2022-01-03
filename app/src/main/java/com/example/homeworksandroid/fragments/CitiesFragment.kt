package com.example.homeworksandroid.fragments

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
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.FORMAT
import com.example.homeworksandroid.R
import com.example.homeworksandroid.activities.MainPageActivity
import com.example.homeworksandroid.adapters.CitiesRecyclerAdapter
import com.example.homeworksandroid.adapters.RecyclerOnCLickListener
import com.example.homeworksandroid.checkNetwork
import com.example.homeworksandroid.viewmodels.CitiesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.choose_city_fragment.*
import kotlinx.android.synthetic.main.put_city_dialog.*
import java.util.*

@AndroidEntryPoint
class CitiesFragment : Fragment(R.layout.choose_city_fragment) {
    companion object {
        fun create() = CitiesFragment()
    }

    private val citiesRecyclerAdapter: CitiesRecyclerAdapter = CitiesRecyclerAdapter(
        RecyclerOnCLickListener { city ->
            changeChosenCity(newChosenName = city.name)

            val cityDate: Date = FORMAT.parse(city.forecastDate) ?: Date(1)

            // TODO: 03.01.2022 forecast deprecated fix 
            if(!DateUtils.isToday(cityDate.time))
                Toast.makeText(requireContext(), "Forecast is deprecated!", Toast.LENGTH_LONG).show()

            val intent = Intent(requireContext(), MainPageActivity::class.java)
            intent.putExtra(getString(R.string.get_city_extra), city)

            startActivity(intent)
        }
    )

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