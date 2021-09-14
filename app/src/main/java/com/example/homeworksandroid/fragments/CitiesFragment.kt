package com.example.homeworksandroid.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.viewmodels.CitiesViewModel
import com.example.homeworksandroid.R
import com.example.homeworksandroid.adapters.CitiesAdapter
import kotlinx.android.synthetic.main.choose_city_fragment.*

const val CITY_KEY = "CITY_KEY"
const val TEMPERATURES = "TEMPERATURES"
const val DESCRIPTIONS = "TEMPERATURES"

class CitiesFragment : Fragment(R.layout.choose_city_fragment) {
    companion object {
        fun create() = CitiesFragment()
    }

    private lateinit var citiesAdapter: CitiesAdapter

    private val viewModel = viewModels<CitiesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.value.citiesLiveData.value.isNullOrEmpty())
            showNoticeDialog()

        setRecyclerView((viewModel.value.citiesLiveData.value ?: linkedSetOf()))

        viewModel.value.citiesLiveData.observe(viewLifecycleOwner) {
            Log.d("MY_ERROR", "onViewCreated: $it")
            updateRecyclerView(it)
            writeCity()
        }

        viewModel.value.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            Log.d("MY_ERROR", "error: $it")
        }

        button_add_city.setOnClickListener {
            showNoticeDialog()
        }
    }

    @SuppressLint("InflateParams")
    private fun showNoticeDialog() {
        AlertDialog.Builder(requireContext()).create().apply {
            val inflater = requireActivity().layoutInflater
            setView(inflater.inflate(R.layout.put_city_dialog, null))
            setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
                val cityInput = findViewById<EditText>(R.id.city_edit_text)?.text.toString()
                viewModel.value.search(cityInput)
            }
            setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { dialog, _ ->
                dialog.cancel()
            }
            show()
        }
    }

    private fun setRecyclerView(cities: LinkedHashSet<CityWeather>) {
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val userRecycle: RecyclerView? = view?.findViewById(R.id.cities_recyclerView)
        userRecycle?.layoutManager = layoutManager
        cities.let {
            citiesAdapter = CitiesAdapter(it)
            userRecycle?.adapter = citiesAdapter
        }

    }

    private fun updateRecyclerView(cities: LinkedHashSet<CityWeather>) {
        cities_recyclerView.adapter?.let {
            val adapter = it as CitiesAdapter
            adapter.updateValues(cities)
        }
    }

    private fun writeCity() {
        val chosen = if(viewModel.value.citiesLiveData.value?.size == 1) 0 else viewModel.value.citiesLiveData.value?.indexOfFirst { it.chosen }
        val city = chosen.let { viewModel.value.citiesLiveData.value?.elementAt(it!!) }
        val name = city?.name + ", " + city?.country
        val temperature = city?.temperatures?.get(0)?.first.toString()
//        val temps = mutableSetOf<String>()
//        val descriptions = mutableSetOf<String>()
//
//        city?.temperatures?.map {
//            temps.add(it.first.toString())
//            descriptions.add(it.second)
//        }

        val pref = activity?.getSharedPreferences(CITY_KEY, Context.MODE_PRIVATE) ?: return
        with(pref.edit()) {
            putString(CITY_KEY, name)
            putString(name, temperature)
            apply()
        }
    }
}