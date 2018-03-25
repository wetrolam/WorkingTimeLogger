package net.useobjects.workingtime.wifi_selection

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_wifi_selection.*
import net.useobjects.workingtime.R
import net.useobjects.workingtime.main.Settings
import net.useobjects.workingtime.utility.GuiThreadTimer
import java.util.concurrent.TimeUnit

class WifiSelectionActivity : AppCompatActivity() {
    private var wifiManager: WifiManager? = null
    private var wifiReceiver: WifiReceiver? = null
    private var timer: GuiThreadTimer? = null

    companion object {
        private val TAG = WifiSelectionActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_selection)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        Log.d(TAG, "onCreate: wifi is " + if (wifiManager!!.isWifiEnabled) "enabled" else "disabled")

        val wifiRecyclerViewAdapter: WifiRecyclerViewAdapter = WifiRecyclerViewAdapter(this)
        val recyclerView: RecyclerView = wifi_list // findViewById(R.id.wifi_list)
        recyclerView.setHasFixedSize(true) // better performance
        recyclerView.setAdapter(wifiRecyclerViewAdapter)

        timer = GuiThreadTimer(5, TimeUnit.SECONDS, Runnable { this.refreshWifiList() })

        wifiReceiver = WifiReceiver(wifiRecyclerViewAdapter)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(wifiReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        timer!!.start()
    }

    override fun onPause() {
        timer!!.stop()
        unregisterReceiver(wifiReceiver)
        super.onPause()
    }

    private fun refreshWifiList() {
        val scanStarterd: Boolean = wifiManager!!.startScan()
        Log.d(TAG, "scanStarterd = $scanStarterd")
    }

    fun onSelected(bssid: String) {
        Log.d(TAG, "selected $bssid")

        val result: Intent = Intent()
        result.putExtra(Settings.BSSID, bssid)
        setResult(Activity.RESULT_OK, result)

        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d(TAG, "onBackPressed")
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}