package com.example.forecast.domain.data_processing

import kotlin.math.roundToInt

object Extensions {
    fun Double.getCelsius() = "${this.roundToInt() - 273}°"
}