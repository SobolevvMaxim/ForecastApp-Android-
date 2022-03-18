package com.example.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

object NetworkUtils {

    fun Fragment.setNetworkListener(offlineModeTextView: TextView) {
        val manager: ConnectivityManager =
            context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val builder = NetworkRequest.Builder()

            builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

            val networkRequest = builder.build()
            manager.registerNetworkCallback(networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        onChangeNetworkState(true, offlineModeTextView)
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        onChangeNetworkState(false, offlineModeTextView)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        onChangeNetworkState(false, offlineModeTextView)
                    }
                })
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }
    }

    fun Fragment.onChangeNetworkState(available: Boolean, offlineModeTextView: TextView) {
        activity?.runOnUiThread {
            when (available) {
                true -> offlineModeTextView.visibility = View.GONE
                false -> offlineModeTextView.visibility = View.VISIBLE
            }
        }
    }


    fun Fragment.isOnline(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
        } else return false

        return false
    }
}