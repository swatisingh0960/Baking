package com.nanodegree.swatisingh.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.nanodegree.swatisingh.baking.R;
import com.nanodegree.swatisingh.baking.activities.BakingActivity;
import com.nanodegree.swatisingh.baking.model.Recipe;

public class WidgetProvider extends AppWidgetProvider{

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds, Recipe recipe){
        for (int appWidgetId : appWidgetIds) {
            CharSequence widgetText = context.getString(R.string.appwidget_text);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_provider);

            Intent widgetServiceIntent = new Intent(context, WidgetServiceRV.class);
            Intent bakingIntent = new Intent(context, BakingActivity.class);

            if (recipe != null){
                widgetServiceIntent.putStringArrayListExtra("bundle", recipe.ingredients);
                widgetServiceIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId + Math.random()), null));
                remoteViews.setTextViewText(R.id.textView_appwidget, widgetText);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, bakingIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_heading, pendingIntent);

            remoteViews.setRemoteAdapter(R.id.listView_widget, widgetServiceIntent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
//        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
//        super.onDisabled(context);
    }

    public static void sendFresh(Context applicationContext, Recipe recipe) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra("bundle", recipe);
        intent.setComponent(new ComponentName(applicationContext, WidgetProvider.class));
        applicationContext.sendBroadcast(intent);
    }


    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            Recipe recipe = intent.getParcelableExtra("bundle");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, WidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.listView_widget);

            updateAppWidget(context, appWidgetManager, appWidgetManager.getAppWidgetIds(componentName), recipe);
            onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(componentName));
        }
    }
}
