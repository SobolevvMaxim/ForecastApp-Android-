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
        val temperaturesSet = pref.getStringSet("${TEMPERATURES}_$cityName", emptySet())
        val descriptionsSet =pref.getStringSet("${DESCRIPTIONS}_$cityName", emptySet())
        Log.d("MY_ERROR", "pref $pref from main city $cityName temps ${temperaturesSet?.size}" +
                " descs ${descriptionsSet?.size}")

        currentCity.text = cityName
        todaysTemp.text = temperaturesSet?.elementAt(0)
        tempDay1.text = temperaturesSet?.elementAt(1)?: todaysTemp.text
        tempDay2.text = temperaturesSet?.elementAt(2)?: tempDay1.text
        tempDay3.text = temperaturesSet?.elementAt(3)?: tempDay2.text
        tempDay4.text = temperaturesSet?.elementAt(4)?: tempDay3.text

        today_sunny.text = descriptionsSet?.elementAt(0)
        sunnyDay1.text = descriptionsSet?.elementAt(1)?: today_sunny.text
        sunnyDay2.text = descriptionsSet?.elementAt(2)?:sunnyDay1.text
        sunnyDay3.text = descriptionsSet?.elementAt(3)?:sunnyDay2.text
        sunnyDay4.text = descriptionsSet?.elementAt(4)?:sunnyDay3.text

    }
}