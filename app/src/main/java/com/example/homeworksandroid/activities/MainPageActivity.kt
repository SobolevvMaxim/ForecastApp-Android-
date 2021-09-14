package com.example.homeworksandroid.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.R
import com.example.homeworksandroid.fragments.CITY_KEY
import com.example.homeworksandroid.fragments.DESCRIPTIONS
import com.example.homeworksandroid.fragments.MainPageFragment
import com.example.homeworksandroid.fragments.TEMPERATURES
import kotlinx.android.synthetic.main.main_page_fragment.*

class MainPageActivity : AppCompatActivity(R.layout.activity_main_page) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            chooseCity()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_main, MainPageFragment.create())
                .commit()
        }


    }

    private fun chooseCity() {
        startActivity(Intent(this, CitiesActivity::class.java))
    }

    override fun onResume() {
        super.onResume()

        val pref = getSharedPreferences(CITY_KEY, Context.MODE_PRIVATE)
        val cityName = pref.getString(CITY_KEY, "ERROR")
        val temperature = pref.getString(cityName, "20")

        currentCity.text = cityName
        todaysTemp.text = temperature


    }
}