package com.example.homeworksandroid.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.homeworksandroid.activities.CitiesActivity
import com.example.homeworksandroid.R

class MainPageFragment : Fragment(R.layout.main_page_fragment) {
    companion object {
        fun create() = MainPageFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageButton>(R.id.mainAddButton).setOnClickListener {
            addCityActivity()
        }
    }

    private fun addCityActivity() {
        startActivity(Intent(context, CitiesActivity::class.java))
    }
}