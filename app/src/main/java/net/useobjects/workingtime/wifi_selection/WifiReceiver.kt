package net.useobjects.workingtime.wifi_selection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log

class WifiReceiver(private val wifiRecyclerViewAdapter: WifiRecyclerViewAdapter) : BroadcastReceiver() {

    companion object {
        private val TAG = WifiReceiver::class.java.simpleName
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: ")
        val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val scanResultList: List<ScanResult> = wifiManager.scanResults
        for (scanResult in scanResultList) {
            Log.d(TAG, " " + scanResult.toString())
        }
        wifiRecyclerViewAdapter.setWifiList(scanResultList)
    }
}
