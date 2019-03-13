package com.ip.insearching.ipaddress

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.widget.RemoteViews
import android.content.Intent
import android.app.PendingIntent
import android.content.ComponentName
import android.util.Log


/**
 * Implementation of App Widget functionality.
 */
class IPAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("MY_TAG", "Got intent " + intent.action)
        if (intent.action == WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, IPAppWidgetProvider::class.java))
            this.onUpdate(context, appWidgetManager, ids)
        } else {
            super.onReceive(context, intent)
        }
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val widgetText = getWifiAddress(context)
            val views = RemoteViews(context.packageName, R.layout.ipapp_widget)
            views.setTextViewText(R.id.appwidget_text, widgetText)
            views.setOnClickPendingIntent(R.id.appwidget_text, getPendingIntent(context, appWidgetId))
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getPendingIntent(context: Context, appWidgetId: Int): PendingIntent {
            val intentUpdate = Intent(context, IPAppWidgetProvider::class.java)
            intentUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val idArray = intArrayOf(appWidgetId)
            intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray)
            return PendingIntent.getBroadcast(
                context, appWidgetId, intentUpdate,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        @SuppressWarnings("deprecation")
        private fun getWifiAddress(context: Context): String {
            val wm = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            return Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
        }
    }
}

