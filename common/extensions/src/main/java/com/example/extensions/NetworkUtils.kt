package com.example.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

object NetworkUtils {

    fun Fragment.setNetworkListener(context: Context?, offlineModeTextView: TextView) {
        val manager: ConnectivityManager =
            context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

    }

    fun Fragment.onChangeNetworkState(available: Boolean, offlineModeTextView: TextView) {
        activity?.runOnUiThread {
            when (available) {
                true -> offlineModeTextView.visibility = View.GONE
                false -> offlineModeTextView.visibility = View.VISIBLE
            }
        }
    }

    fun isConnected(): Boolean {
        try {
            val command = "ping -c 1 google.com"
            return Runtime.getRuntime().exec(command).waitFor() == 0
        } catch (e: Exception) {

        }
        return false
    }
}