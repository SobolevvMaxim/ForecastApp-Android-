package com.example.homeworksandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainPageActivity : AppCompatActivity(R.layout.activity_main_page) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainPageFragment.create())
                .commit()
        }
    }
}