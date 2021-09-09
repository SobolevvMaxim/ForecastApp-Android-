package com.example.homeworksandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CitiesActivity : AppCompatActivity(R.layout.acitvity_choose_city) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_cities, CitiesFragment.create())
                .commit()
        }
    }
}