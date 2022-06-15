package com.example.forecast.feature_settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.forecast.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(R.layout.settings_activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        NavUtils.navigateUpFromSameTask(this)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
            val key = preference.key
            when {
                key == getString(R.string.key_temperature_unit) && newValue is String -> {
                    val temperatureUnit = TemperatureUnit.fromString(newValue)

                    Toast.makeText(context, "Unit changed to $temperatureUnit", Toast.LENGTH_LONG)
                        .show()
                }
                key == getString(R.string.key_auto_update) && newValue is String -> {


                    Toast.makeText(context, "AutoUpdate changed to $newValue", Toast.LENGTH_LONG)
                        .show()
                }
            }
            return true
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            findPreference<Preference>(getString(R.string.key_temperature_unit))!!.onPreferenceChangeListener =
                this
            findPreference<Preference>(getString(R.string.key_auto_update))!!.onPreferenceChangeListener =
                this

            findPreference<Preference>("About")!!.setOnPreferenceClickListener {
                Intent(Intent.ACTION_VIEW)
                    .apply { data = Uri.parse("https://github.com/SobolevvMaxim/ForecastApp-Android-") }
                    .let { startActivity(it); true }
            }
        }
    }
}