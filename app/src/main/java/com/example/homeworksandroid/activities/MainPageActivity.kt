package com.example.homeworksandroid.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.homeworksandroid.R
import com.example.homeworksandroid.fragments.MainPageFragment

class MainPageActivity : AppCompatActivity(R.layout.activity_main_page) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list = intent.getSerializableExtra("TEMPERATURE_ARRAY")
        Log.d("MY_ERROR", "list get in main $list")

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
}