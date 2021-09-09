package com.example.homeworksandroid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class CitiesFragment : Fragment(R.layout.choose_city_fragment) {
    companion object {
        fun create() = CitiesFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}