package com.mathematicalfunk.appterminator.widget

import android.app.ActivityManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.drawable.BitmapDrawable
import android.widget.RemoteViews
import android.widget.Toast
import com.mathematicalfunk.appterminator.R
import java.util.*

const val WIDGET_ID = "widget_id"
const val APPLICATION_INDEX = "application_index"
private const val KILL_CLICKED = "kill_app"
private const val IMAGE_CLICKED = "image_click"

class TerminatorWidgetProvider : AppWidgetProvider() {

    private var debugApps: MutableList<ApplicationInfo> = ArrayList()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val packageManager = context.packageManager
        val installedApplications = packageManager.getInstalledApplications(0)

        for (applicationInfo in installedApplications) {
            val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
            if (isDebuggable) {
                debugApps.add(applicationInfo)
            }
        }
        for (widgetId in appWidgetIds) {
            val remoteViews = RemoteViews(context.packageName, R.layout.widget)
            setupRemoteViews(context, appWidgetManager, remoteViews, widgetId, 0)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val remoteViews = RemoteViews(context.packageName, R.layout.widget)
        val widgetId = intent.getIntExtra(WIDGET_ID, 0)
        var appIndex = intent.getIntExtra(APPLICATION_INDEX, 0)
        if (KILL_CLICKED == intent.action) {
            val applicationInfo = debugApps[appIndex]
            val systemService = context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            systemService.killBackgroundProcesses(applicationInfo.packageName)
            Toast.makeText(context, context.getString(R.string.kill_process, applicationInfo.packageName), Toast.LENGTH_SHORT).show()
        } else if (IMAGE_CLICKED == intent.action) {
            appIndex = if (++appIndex < debugApps.size - 1) appIndex else 0
            setupRemoteViews(context, appWidgetManager, remoteViews, widgetId, appIndex)
        }
    }

    private fun setupRemoteViews(context: Context, appWidgetManager: AppWidgetManager, remoteViews: RemoteViews, widgetId: Int, appIndex: Int) {
        val applicationInfo = debugApps[appIndex]
        val drawable = applicationInfo.loadIcon(context.packageManager)
        val bitmap = (drawable as BitmapDrawable).bitmap
        remoteViews.setImageViewBitmap(R.id.widget_image, bitmap)
        remoteViews.setTextViewText(R.id.textView, applicationInfo.packageName)
        remoteViews.setOnClickPendingIntent(R.id.actionButton, getPendingSelfIntent(context, KILL_CLICKED, widgetId, appIndex))
        remoteViews.setOnClickPendingIntent(R.id.widget_image, getPendingSelfIntent(context, IMAGE_CLICKED, widgetId, appIndex))
        appWidgetManager.updateAppWidget(widgetId, remoteViews)
    }

    private fun getPendingSelfIntent(context: Context?, action: String?, widgetId: Int, appIndex: Int): PendingIntent {
        val intent = Intent(context, javaClass)
        intent.action = action
        intent.putExtra(WIDGET_ID, widgetId)
        intent.putExtra(APPLICATION_INDEX, appIndex)
        return PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

}