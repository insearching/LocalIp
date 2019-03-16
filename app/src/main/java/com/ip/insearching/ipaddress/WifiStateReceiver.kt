package com.ip.insearching.ipaddress

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import timber.log.Timber

class WifiStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Timber.i("Received intent %s", intent.action)

        val appWidgetIds = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, IPAppWidgetProvider::class.java))

        context.sendBroadcast(
            Intent(context, IPAppWidgetProvider::class.java)
                .apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                })
    }
}