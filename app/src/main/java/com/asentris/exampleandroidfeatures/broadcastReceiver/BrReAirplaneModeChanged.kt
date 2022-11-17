package com.asentris.exampleandroidfeatures.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast

class BrReAirplaneModeChanged : BroadcastReceiver() {

    private var isRegistered = false

    override fun onReceive(context: Context?, intent: Intent?) {
        val isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?: return
        val text = if (isAirplaneModeEnabled) "enabled" else "disabled"
        Toast.makeText(context, "Airplane mode $text", Toast.LENGTH_SHORT).show()
    }

    fun register(context: Context?) {
        if (!isRegistered) {
            context?.registerReceiver(this, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
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

// https://developer.android.com/reference/android/content/Intent#ACTION_AIRPLANE_MODE_CHANGED