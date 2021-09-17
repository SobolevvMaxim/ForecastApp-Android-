package com.example.homeworksandroid.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.homeworksandroid.activities.CitiesActivity
import com.example.homeworksandroid.R
import com.example.homeworksandroid.viewmodels.MainPageViewModel

class MainPageFragment : Fragment(R.layout.main_page_fragment) {
    companion object {
        fun create() = MainPageFragment()
    }

    private val viewModel = viewModels<MainPageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.value.citiesLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Log.d("MY_ERROR", "city got in fragment: $it ")
            }
        }

        viewModel.value.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
            Log.d("MY_ERROR", "ERROR: $it ")
        }

        view.findViewById<ImageButton>(R.id.mainAddButton).setOnClickListener {
            addCityActivity()
        }
    }

    private fun addCityActivity() {
        startActivity(Intent(context, CitiesActivity::class.java))
    }

    override fun onResume() {
        super.onResume()

        viewModel.value.getCurrentCity()
    }
}