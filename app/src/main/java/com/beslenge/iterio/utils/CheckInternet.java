package com.beslenge.iterio.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import com.beslenge.iterio.Trigger;

public class CheckInternet {
    public CheckInternet(Context context, Trigger trigger) {
        trigger.trigger(checkNetwork(context));
    }
    
    private boolean checkNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = cm.getAllNetworks();
        boolean isConnected = false;
        for (Network network : networks) {
            NetworkCapabilities networkCapabilities = cm.getNetworkCapabilities(network);
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                isConnected = true;
                break;
            }
        }
        return isConnected;
    }
}
