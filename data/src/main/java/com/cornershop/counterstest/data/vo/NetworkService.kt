package com.cornershop.counterstest.data.vo


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

val NetworkError ="checkinternet"

@RequiresApi(Build.VERSION_CODES.M)
fun isOnline(c: Context):Boolean{
    var connectivityManager: ConnectivityManager = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var networkInfo = connectivityManager.activeNetwork
    val actNw = connectivityManager.getNetworkCapabilities(networkInfo) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        //for other device how are able to connect with Ethernet
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        //for check internet over Bluetooth
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
        else -> false
    }

}

