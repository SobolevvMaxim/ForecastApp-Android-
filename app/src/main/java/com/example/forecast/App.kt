package com.example.forecast

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun checkNetwork(context: Context?): Boolean {
    val manager: ConnectivityManager =
        context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val builder = NetworkRequest.Builder()
    builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
    var result = false

    val networkRequest = builder.build()
    manager.registerNetworkCallback(networkRequest,
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.i("Test", "Network Available")
                result = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.i("Test", "Connection lost")
            }
        })
    return result
}