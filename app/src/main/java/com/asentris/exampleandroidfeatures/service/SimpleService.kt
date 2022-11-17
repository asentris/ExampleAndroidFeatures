package com.asentris.exampleandroidfeatures.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/** Manifest defines service */
class SimpleService : Service() {

    private val TAG: String = this::class.java.simpleName

    init {
        Log.d(TAG, "Service::init")
    }

    override fun onBind(intent: Intent?): IBinder? = null // used if multiple clients want to bind

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dataString: String? = intent?.getStringExtra("EXTRA_DATA")
        dataString?.let { Log.d(TAG, dataString) }

        // if system kills service, it won't be recreated
        val startNotSticky = START_NOT_STICKY
        // if system kills service, it'll be recreated when possible, but intent won't be redelivered (instead, null will)
        val startSticky = START_STICKY
        // if system kills service, it'll be recreated when possible and intent will be redelivered
        val startRedeliverIntent = START_REDELIVER_INTENT
        return startSticky
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service::onDestroy")
    }
}