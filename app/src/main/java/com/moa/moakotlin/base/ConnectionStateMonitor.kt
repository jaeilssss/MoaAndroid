package com.moa.moakotlin.base

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest


class ConnectionStateMonitor(val activity : Activity,val onConnected:()->Unit,val onDisconnected:()->Unit) : NetworkCallback() {

    private val networkRequest : NetworkRequest = NetworkRequest.Builder().build()
    private val manager : NetworkInfo
    val connectivityManager : ConnectivityManager
    init {
        connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest,this)
        manager = connectivityManager.activeNetworkInfo!!

    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        activity.runOnUiThread {
            manager.type
            onConnected()
        }
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        activity.runOnUiThread { onDisconnected() } } }

