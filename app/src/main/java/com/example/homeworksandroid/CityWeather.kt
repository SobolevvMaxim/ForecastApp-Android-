package com.example.homeworksandroid

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.*
import com.example.homeworksandroid.database.TemperatureConverter
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

const val TABLE_NAME = "CITIES_TABLE"
@SuppressLint("ConstantLocale")
val FORMAT = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

@Parcelize
@Entity(tableName = TABLE_NAME)
data class CityWeather(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "country") val country: String,
    var lat: String = "",
    var lon: String = "",
    @TypeConverters(TemperatureConverter::class) var temperatures: ArrayList<DailyForecast> = mutableListOf<DailyForecast>() as ArrayList<DailyForecast>,
    var chosen: Boolean = false,
    @ColumnInfo(name = "forecastDate") val forecastDate: String
): Parcelable {
    private companion object: Parceler<CityWeather> {
        override fun create(parcel: Parcel): CityWeather {
            val list: ArrayList<String> = parcel.createStringArrayList() as ArrayList<String>
            val temperaturesNew = mutableListOf<DailyForecast>()
            Log.d("ParcebleTest", "list: $list")
            list.forEach {
                val values = it.split(", ")
                temperaturesNew.add(DailyForecast(temp = values[0].toInt(), description = values[1]))
            }
            val city = CityWeather(
                id = parcel.readString()!!,
                name = parcel.readString()!!,
                country = parcel.readString()!!,
                lat = parcel.readString()!!,
                lon = parcel.readString()!!,
                temperatures = temperaturesNew as ArrayList,
                chosen = true,
                forecastDate = parcel.readString()!!
            )
            Log.d("ParcebleTest", "create: $city")
            return city
        }

        override fun CityWeather.write(parcel: Parcel, flags: Int) {
            val newTemperatures = mutableListOf<String>()
            temperatures.forEach { newTemperatures.add("${it.temp}, ${it.description}") }
            Log.d("ParcebleTest", "write list $newTemperatures")
            parcel.writeStringList(newTemperatures)
            parcel.writeString(id)
            parcel.writeString(name)
            parcel.writeString(country)
            parcel.writeString(lat)
            parcel.writeString(lon)
            parcel.writeString(forecastDate)
        }
    }
}

@Parcelize
data class DailyForecast(val temp: Int, val description: String): Parcelable {
    companion object: Parceler<DailyForecast> {
        override fun create(parcel: Parcel): DailyForecast {
            return DailyForecast(
                temp = parcel.readInt(),
                description = parcel.readString()!!
            )
        }

        override fun DailyForecast.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(temp)
            parcel.writeString(description)
        }
    }
}