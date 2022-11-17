package com.asentris.exampleandroidfeatures.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast

class BrReBatteryChanged : BroadcastReceiver() {

    private var isRegistered = false

    override fun onReceive(context: Context?, intent: Intent?) {
        val text = when(intent?.action) {
            Intent.ACTION_POWER_CONNECTED -> "Power connected"
            Intent.ACTION_POWER_DISCONNECTED -> "Power disconnected"
            Intent.ACTION_BATTERY_OKAY -> "Battery okay"
            Intent.ACTION_BATTERY_LOW -> "Battery low"
            else -> return
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun register(context: Context?) {
        if (!isRegistered) {
            context?.registerReceiver(this, IntentFilter(Intent.ACTION_POWER_CONNECTED))
            context?.registerReceiver(this, IntentFilter(Intent.ACTION_POWER_DISCONNECTED))
            context?.registerReceiver(this, IntentFilter(Intent.ACTION_BATTERY_OKAY))
            context?.registerReceiver(this, IntentFilter(Intent.ACTION_BATTERY_LOW))
            isRegistered = true
        }
    }

    fun unregister(context: Context?) {
        if(isRegistered) {
            context?.unregisterReceiver(this)
            isRegistered = false
        }
    }
}

// https://developer.android.com/reference/android/content/Intent#ACTION_BATTERY_CHANGED
// https://developer.android.com/reference/android/os/BatteryManager