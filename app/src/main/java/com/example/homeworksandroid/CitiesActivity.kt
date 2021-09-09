package com.example.homeworksandroid

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class CitiesActivity : AppCompatActivity(R.layout.acitvity_choose_city) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_cities, CitiesFragment.create())
                .commit()
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return false
            }
        }
        return super.onContextItemSelected(item)
    }
}