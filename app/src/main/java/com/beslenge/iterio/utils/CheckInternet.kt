package com.beslenge.iterio.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.beslenge.iterio.Trigger

class CheckInternet(context: Context, trigger: Trigger) {
    private fun checkNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networks = cm.allNetworks
        var isConnected = false
        for (network in networks) {
            val networkCapabilities = cm.getNetworkCapabilities(network)
            if (networkCapabilities!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                isConnected = true
                break
            }
        }
        return isConnected
    }

    init {
        trigger.trigger(checkNetwork(context))
    }
}