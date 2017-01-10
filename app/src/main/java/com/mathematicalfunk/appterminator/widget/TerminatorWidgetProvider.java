package com.mathematicalfunk.appterminator.widget;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.mathematicalfunk.appterminator.R;

public class TerminatorWidgetProvider extends AppWidgetProvider {
    public static final String WIDGET_ID = "widget_id";
    public static final String APPLICATION_INDEX = "application_index";
    private static final String KILL_CLICKED = "kill_app";
    private static final String IMAGE_CLICKED = "image_click";
    private static List<ApplicationInfo> debugApps;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
        debugApps = new ArrayList<>();
        for (ApplicationInfo applicationInfo : installedApplications) {
            boolean isDebuggable = (0 != (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
            if (isDebuggable) {
                debugApps.add(applicationInfo);
            }
        }

        for(int widgetId : appWidgetIds){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            setupRemoteViews(context, appWidgetManager, remoteViews, widgetId, 0);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        int widgetId = intent.getIntExtra(WIDGET_ID, 0);
        int appIndex = intent.getIntExtra(APPLICATION_INDEX, 0);

        if (KILL_CLICKED.equals(intent.getAction())) {
            ApplicationInfo applicationInfo = debugApps.get(appIndex);
            ActivityManager systemService = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            systemService.killBackgroundProcesses(applicationInfo.packageName);
            Toast.makeText(context, context.getString(R.string.kill_process, applicationInfo.packageName), Toast.LENGTH_SHORT).show();
        } else if (IMAGE_CLICKED.equals(intent.getAction())) {
            appIndex = ++appIndex < debugApps.size() - 1 ? appIndex : 0;
            setupRemoteViews(context, appWidgetManager, remoteViews, widgetId, appIndex);
        }
    }

    private void setupRemoteViews(Context context, AppWidgetManager appWidgetManager, RemoteViews remoteViews, int widgetId, int appIndex) {
        ApplicationInfo applicationInfo = debugApps.get(appIndex);

        Drawable drawable = applicationInfo.loadIcon(context.getPackageManager());
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        remoteViews.setImageViewBitmap(R.id.widget_image, bitmap);

        remoteViews.setTextViewText(R.id.textView, applicationInfo.packageName);

        remoteViews.setOnClickPendingIntent(R.id.actionButton, getPendingSelfIntent(context, KILL_CLICKED, widgetId, appIndex));
        remoteViews.setOnClickPendingIntent(R.id.widget_image, getPendingSelfIntent(context, IMAGE_CLICKED, widgetId, appIndex));

        appWidgetManager.updateAppWidget(widgetId, remoteViews);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action, int widgetId, int appIndex) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra(WIDGET_ID, widgetId);
        intent.putExtra(APPLICATION_INDEX, appIndex);
        return PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}