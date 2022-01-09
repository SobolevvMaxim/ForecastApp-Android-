package com.example.forecast.feature_forecast.domain.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

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