package com.example.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkManager(context: Context?, onChangeNetworkState: (Boolean) -> Unit) {
    private val networkListener =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Timber.d("Network available")
                onChangeNetworkState(true)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Timber.d("Network unavailable")
                onChangeNetworkState(false)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Timber.d("Network lost")
                onChangeNetworkState(false)
            }
        }

    private val networkManager: ConnectivityManager by lazy {
        context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkRequest: NetworkRequest =
        NetworkRequest.Builder().apply {
            addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        }.build()

    fun registerNetworkListener() {
        networkManager.registerNetworkCallback(networkRequest, networkListener)
    }

    fun unregisterNetworkListener() {
        networkManager.unregisterNetworkCallback(networkListener)
    }
}