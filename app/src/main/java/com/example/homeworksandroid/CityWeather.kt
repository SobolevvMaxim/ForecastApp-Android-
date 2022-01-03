package com.example.homeworksandroid

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.example.homeworksandroid.database.TABLE_NAME
import com.example.homeworksandroid.database.TemperatureConverter
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
@Entity(tableName = TABLE_NAME)
data class CityWeather(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "country") val country: String,
    var lat: String = "",
    var lon: String = "",
    @TypeConverters(TemperatureConverter::class) var temperatures: ArrayList<Daily>,
    var chosen: Boolean = false,
    @ColumnInfo(name = "forecastDate") val forecastDate: String,
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
}

@Parcelize
data class Daily(val temp: Int, val description: String) : Parcelable {
    companion object : Parceler<Daily> {
        override fun create(parcel: Parcel): Daily =
            Daily(
                temp = parcel.readInt(),
                description = parcel.readString() ?: "none"
            )


        override fun Daily.write(parcel: Parcel, flags: Int) {
            parcel.apply {
                writeInt(temp)
                writeString(description)
            }
        }
    }
}