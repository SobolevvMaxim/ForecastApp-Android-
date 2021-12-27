package com.example.homeworksandroid.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.DailyForecast
import com.example.homeworksandroid.R
import com.example.homeworksandroid.fragments.MainPageFragment

const val GET_CHOSEN_CITY = "CHOSEN_CITY"
const val P_LOG = "PARCELABLE_LOG"

class MainPageActivity : AppCompatActivity(R.layout.activity_main_page) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val chosenCity: CityWeather = intent.getParcelableExtra(GET_CHOSEN_CITY) ?: CityWeather(
                id = "",
                name = "",
                country = "",
                lat = "",
                lon = "",
                temperatures = ArrayList(),
                chosen = true,
                forecastDate = "01.01.2021"
            )
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_main, MainPageFragment.create(city = chosenCity))
                .commit()
        }
    }

}