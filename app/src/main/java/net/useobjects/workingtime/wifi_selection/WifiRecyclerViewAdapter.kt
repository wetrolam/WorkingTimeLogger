package net.useobjects.workingtime.wifi_selection

import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.wifi_list_item.view.*
import net.useobjects.workingtime.R
import java.util.*

class WifiRecyclerViewAdapter(private val activity: WifiSelectionActivity) : RecyclerView.Adapter<WifiRecyclerViewAdapter.ViewHolder>() {

    private var wifiList: List<ScanResult> = ArrayList()

    companion object {
        private val TAG = WifiRecyclerViewAdapter::class.java.simpleName
    }

    override fun getItemCount(): Int {
        return wifiList.size
    }

    fun setWifiList(newWifiList: List<ScanResult>) {
        wifiList = newWifiList.sortedByDescending { it.level }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ssid: TextView
        val bssid: TextView
        val level: TextView
        val level_percent: TextView

        init {
            itemView.setOnClickListener{ this.onWifiSelected() }
            ssid  = itemView.wifi_ssid  //  itemView.findViewById(R.id.wifi_ssid)
            bssid = itemView.wifi_bssid // itemView.findViewById(R.id.wifi_bssid)
            level = itemView.wifi_level // itemView.findViewById(R.id.wifi_level)
            level_percent = itemView.wifi_level_in_percent // itemView.findViewById(R.id.wifi_level_in_percent)
        }

        private fun onWifiSelected() {
            val seletedBssid: String = bssid.text.toString()
            Log.d(TAG, "selected: $seletedBssid")
            activity.onSelected(seletedBssid)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.wifi_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scanResult = wifiList[position]
        holder.ssid.text = scanResult.SSID
        holder.bssid.text = scanResult.BSSID
        holder.level.text = Integer.toString(scanResult.level)
        holder.level_percent.text = String.format("%d%%", WifiManager.calculateSignalLevel(scanResult.level, 100))
    }
}
