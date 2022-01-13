package com.example.forecast.feature_forecast.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.example.forecast.feature_forecast.data.local.entities.CityWeatherEntity
import com.example.forecast.feature_forecast.data.remote.dto.Coord
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class CityWeather(
    val id: String,
    val name: String,
    val country: String,
    var lat: String = "",
    var lon: String = "",
    var temperatures: ArrayList<Daily>,
    var chosen: Boolean = false,
    val forecastDate: String,
) : Parcelable {
    private companion object : Parceler<CityWeather> {
        override fun create(parcel: Parcel): CityWeather {
            val parcelList: ArrayList<String> = parcel.createStringArrayList() as ArrayList<String>

            val dailyList: List<Daily> = parcelList.map {
                val values = it.split(", ")
                Daily(
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

    fun toCityWeatherEntity(): CityWeatherEntity = CityWeatherEntity(
        id,
        name,
        country,
        lat,
        lon,
        temperatures,
        chosen,
        forecastDate
    )

    fun toCity() = City(
        coord = Coord(
            lat = lat,
            lon = lon
        ),
        id = id,
        name = name,
        country = country
    )
}