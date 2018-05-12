package net.useobjects.workingtime

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import net.useobjects.workingtime.main.Settings
import net.useobjects.workingtime.wifi_selection.WifiSelectionActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
        private const val SELECT_WIFI_REQUEST: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = getSharedPreferences(Settings.preferenceFile, Context.MODE_PRIVATE)
        val bssid = preferences.getString(Settings.BSSID, "")
        selected_bssid.text = bssid

        select_wifi.setOnClickListener { selectWifi() }
    }

    private fun selectWifi() {
        Log.d(TAG, "select wifi")
        startActivityForResult(Intent(this, WifiSelectionActivity::class.java), SELECT_WIFI_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult")

        if( requestCode == SELECT_WIFI_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val bssid: String = data!!.getStringExtra(Settings.BSSID)

            val preferences = getSharedPreferences(Settings.preferenceFile, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(Settings.BSSID, bssid)
            editor.commit()

            Snackbar.make(selected_bssid, "selected $bssid", Snackbar.LENGTH_SHORT).show()
            selected_bssid.text = "selected $bssid"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // R.id.settings ->
            R.id.select_wifi -> {
                selectWifi()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
