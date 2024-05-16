package com.example.broadcastreceiverapp

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.Settings
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.broadcastreceiverapp.ui.theme.BroadcastReceiverAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.registerReceiver(bReceiver, IntentFilter())
        registerIntentFilters(this@MainActivity)
        setContent {
            BroadcastReceiverAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
         val bReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_AIRPLANE_MODE_CHANGED-> {
                        val isAirplaneModeEnabled = Settings.System.getInt(context?.contentResolver,
                            Settings.Global.AIRPLANE_MODE_ON, 0) != 0
                        if (isAirplaneModeEnabled) {
                            Toast.makeText(context, "ACTION_AIRPLANE_MODE_CHANGED ENABLED", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "ACTION_AIRPLANE_MODE_CHANGED DISABLED", Toast.LENGTH_LONG).show()
                        }
                    }
                    Intent.ACTION_BATTERY_CHANGED -> {
                        val batteryStatus: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                        val isCharging: Boolean = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING ||
                                batteryStatus == BatteryManager.BATTERY_STATUS_FULL
                        if (isCharging) {
                            Toast.makeText(context, "ACTION_BATTERY_CHANGED: Device is charging", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "ACTION_BATTERY_CHANGED: Device is not charging", Toast.LENGTH_LONG).show()
                        }
                    }
                    Intent.ACTION_POWER_CONNECTED -> {
                        Toast.makeText(context, "ACTION_POWER_CONNECTED: Power connected", Toast.LENGTH_LONG).show()
                    }
                    Intent.ACTION_POWER_DISCONNECTED -> {
                        Toast.makeText(context, "ACTION_POWER_DISCONNECTED: Power disconnected", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

private val bReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED-> {
                val isAirplaneModeEnabled = Settings.System.getInt(context?.contentResolver,
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0
                if (isAirplaneModeEnabled) {
                    Toast.makeText(context, "ACTION_AIRPLANE_MODE_CHANGED ENABLED", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "ACTION_AIRPLANE_MODE_CHANGED DISABLED", Toast.LENGTH_LONG).show()
                }
            }
            Intent.ACTION_BATTERY_CHANGED -> {
                val batteryStatus: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val isCharging: Boolean = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING ||
                        batteryStatus == BatteryManager.BATTERY_STATUS_FULL
                if (isCharging) {
                    Toast.makeText(context, "ACTION_BATTERY_CHANGED: Device is charging", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "ACTION_BATTERY_CHANGED: Device is not charging", Toast.LENGTH_LONG).show()
                }
            }
            Intent.ACTION_POWER_CONNECTED -> {
                Toast.makeText(context, "ACTION_POWER_CONNECTED: Power connected", Toast.LENGTH_LONG).show()
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                Toast.makeText(context, "ACTION_POWER_DISCONNECTED: Power disconnected", Toast.LENGTH_LONG).show()
            }
        }
    }
}

private fun registerIntentFilters(
    activity: Activity
) {
    activity.registerReceiver(bReceiver, IntentFilter().apply {
        addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        addAction(Intent.ACTION_BATTERY_CHANGED)
        addAction(Intent.ACTION_POWER_CONNECTED)
        addAction(Intent.ACTION_POWER_DISCONNECTED)
    })
}