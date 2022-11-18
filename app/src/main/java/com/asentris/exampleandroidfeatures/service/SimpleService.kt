package com.asentris.exampleandroidfeatures.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

/** Manifest defines service */
class SimpleService : Service() {

    private val TAG: String = this::class.java.simpleName
    private var job: Job? = null

    init {
        Log.d(TAG, "Service::init")
    }

    override fun onBind(intent: Intent?): IBinder? = null // used if multiple clients want to bind

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dataString: String? = intent?.getStringExtra("EXTRA_DATA")
        dataString?.let { Log.d(TAG, dataString) }

        // run long running task on different thread
        Thread {
            // if statement prevents starting multiple coroutines since onStartCommand can run many times
            // alternatively, the job can be cancelled and restarted if desired
            if (job == null) job = CoroutineScope(Dispatchers.IO).launch {
                var count = 0
                while (true) {
                    Log.d(TAG, "Service count: $count")
                    count++
                    delay(2000)
                }
            }
        }.start()

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
        job?.cancel()
        Log.d(TAG, "Service::onDestroy")
    }
}