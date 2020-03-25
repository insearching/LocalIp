package com.ip.insearching.ipaddress

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.widget.RemoteViews
import timber.log.Timber


/**
 * Implementation of App Widget functionality.
 */
class IPAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Timber.i("Received intent %s", intent.action)
        if (intent.action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {
            with(AppWidgetManager.getInstance(context)) {
                onUpdate(context, this, getAppWidgetIds(ComponentName(context, IPAppWidgetProvider::class.java)))
            }
        } else {
            super.onReceive(context, intent)
        }
    }

    companion object {

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            with(RemoteViews(context.packageName, R.layout.ipapp_widget)) {
                setTextViewText(R.id.appwidget_text, retrieveWifiAddress(context))
                setOnClickPendingIntent(R.id.appwidget_text, getPendingIntent(context, appWidgetId))
                appWidgetManager.updateAppWidget(appWidgetId, this)
            }
        }

        @SuppressWarnings("deprecation")
        private fun retrieveWifiAddress(context: Context): String {
            val wifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        }

        private fun getPendingIntent(context: Context, appWidgetId: Int): PendingIntent {
            val intentUpdate = Intent(context, IPAppWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
            }
            return PendingIntent.getBroadcast(
                context,
                appWidgetId,
                intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }
}
