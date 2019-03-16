package com.ip.insearching.ipaddress

import android.app.Application
import android.content.IntentFilter
import android.net.wifi.WifiManager

class IPApp : Application() {

    override fun onCreate() {
        super.onCreate()

        registerReceiver(WifiStateReceiver(), IntentFilter().apply {
            addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        })
    }
}