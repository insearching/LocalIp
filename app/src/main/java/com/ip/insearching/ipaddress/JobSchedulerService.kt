package com.ip.insearching.ipaddress

import android.app.job.JobParameters
import android.app.job.JobService
import android.appwidget.AppWidgetManager
import android.content.*
import android.util.Log
import android.content.IntentFilter

class JobSchedulerService : JobService() {

    companion object {
        const val WIFI_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_STATE_CHANGE"
        const val STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE"
    }

    private val receiver: WifiStateReceiver = WifiStateReceiver()

    override fun onStartJob(params: JobParameters): Boolean {
        registerReceiver(receiver, IntentFilter().also {
            it.addAction(WIFI_STATE_CHANGED_ACTION)
            it.addAction(STATE_CHANGED_ACTION)
        })
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        unregisterReceiver(receiver)
        return true
    }

    class WifiStateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.e("MY_TAG", "WifiStateReceiver Got intent " + intent.action)
            if (intent.action == WIFI_STATE_CHANGED_ACTION ||
                intent.action == STATE_CHANGED_ACTION) {
                val appWidgetIds = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(ComponentName(context, IPAppWidgetProvider::class.java))

                context.sendBroadcast(Intent(context, IPAppWidgetProvider::class.java)
                    .also {
                        it.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                        it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                    })
            }
        }
    }
}