package com.example.homeworksandroid.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.viewmodels.CitiesViewModel
import com.example.homeworksandroid.R
import com.example.homeworksandroid.activities.MainPageActivity
import com.example.homeworksandroid.adapters.CitiesAdapter
import kotlinx.android.synthetic.main.choose_city_fragment.*
import kotlinx.coroutines.delay

class CitiesFragment : Fragment(R.layout.choose_city_fragment) {
    companion object {
        fun create() = CitiesFragment()
    }

    private lateinit var citiesAdapter: CitiesAdapter

    private val viewModel = viewModels<CitiesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.value.getAddedCities()
        }

        viewModel.value.citiesLiveData.observe(viewLifecycleOwner) {
            Log.d("MY_ERROR", "onViewCreated: $it")
            if(it.isEmpty())
                showNoticeDialog()
            updateRecyclerView(it)
        }

        viewModel.value.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            Log.d("MY_ERROR", "error: $it")
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
                val cityInput = findViewById<EditText>(R.id.city_edit_text)?.text.toString()
                viewModel.value.search(cityInput)
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
        val userRecycle: RecyclerView? = view?.findViewById(R.id.cities_recyclerView)
        userRecycle?.layoutManager = layoutManager

        citiesAdapter = CitiesAdapter()
        userRecycle?.adapter = citiesAdapter


    }

    private fun updateRecyclerView(cities: Set<CityWeather>) {
        cities_recyclerView.adapter?.let {
            val adapter = it as CitiesAdapter
            adapter.updateValues(cities)
        }
    }
}