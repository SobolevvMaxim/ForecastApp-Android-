package com.example.homeworksandroid

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.room.*
import com.example.homeworksandroid.activities.P_LOG
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
    @TypeConverters(TemperatureConverter::class) var temperatures: ArrayList<DailyForecast>,
    var chosen: Boolean = false,
    @ColumnInfo(name = "forecastDate") val forecastDate: String
) : Parcelable {
    private companion object : Parceler<CityWeather> {
        override fun create(parcel: Parcel): CityWeather {
            val parcelList: ArrayList<String> = parcel.createStringArrayList() as ArrayList<String>

            val dailyList: List<DailyForecast> = parcelList.map {
                val values = it.split(", ")
                DailyForecast(
                    temp = values[0].toInt(),
                    description = values[1]
                )
            }

            return CityWeather(
                id = parcel.readString() ?: "",
                name = parcel.readString() ?: "Default",
                country = parcel.readString() ?: "NN",
                lat = parcel.readString() ?: "111",
                lon = parcel.readString() ?: "222",
                temperatures = dailyList as ArrayList,
                chosen = true,
                forecastDate = parcel.readString() ?: "01.01.2021"
            )
        }

        override fun CityWeather.write(parcel: Parcel, flags: Int) {
            val dailyList = temperatures.map { "${it.temp}, ${it.description}" }

            parcel.apply {
                writeStringList(dailyList)
                writeString(id)
                writeString(name)
                writeString(country)
                writeString(lat)
                writeString(lon)
                writeString(forecastDate)
            }
        }
    }
}

@Parcelize
data class DailyForecast(val temp: Int, val description: String) : Parcelable {
    companion object : Parceler<DailyForecast> {
        override fun create(parcel: Parcel): DailyForecast =
            DailyForecast(
                temp = parcel.readInt(),
                description = parcel.readString() ?: "none"
            )


        override fun DailyForecast.write(parcel: Parcel, flags: Int) {
            parcel.apply {
                writeInt(temp)
                writeString(description)
            }
        }
    }
}