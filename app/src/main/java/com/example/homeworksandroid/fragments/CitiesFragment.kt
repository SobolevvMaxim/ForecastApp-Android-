package com.example.homeworksandroid.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
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
import kotlinx.android.synthetic.main.put_city_dialog.*
import okhttp3.internal.toImmutableList

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

    override fun onDestroyView() {
        super.onDestroyView()
        val city = viewModel.value.citiesLiveData.value?.elementAt(0)
        val list = LinkedHashSet(viewModel.value.citiesLiveData.value)
        startActivity(Intent(context, MainPageActivity::class.java).apply {
            putExtra("CITY_NAME", city?.name + ", " + city?.country)
            putExtra("TEMPERATURE_ARRAY", list)
        })
        // TODO: 14.09.2021 GetCity to main activity
    }
}