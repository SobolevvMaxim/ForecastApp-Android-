package com.example.homeworksandroid.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.homeworksandroid.CityWeather
import com.example.homeworksandroid.R
import com.example.homeworksandroid.fragments.MainPageFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val P_LOG = "PARCELABLE_LOG"

@AndroidEntryPoint
class MainPageActivity : AppCompatActivity(R.layout.activity_main_page) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (savedInstanceState == null) {
            val chosenCity: CityWeather = intent.getParcelableExtra(getString(R.string.get_city_extra)) ?: CityWeather(
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
                .replace(R.id.container_main, MainPageFragment.create(city = chosenCity, city_tag = getString(R.string.get_city_extra)))
                .commit()
        }
    }
}