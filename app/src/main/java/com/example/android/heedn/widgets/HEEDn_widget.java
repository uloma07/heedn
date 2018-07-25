package com.example.android.heedn.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.android.heedn.R;
import com.example.android.heedn.ReviewScriptureActivity;
import com.example.android.heedn.ScriptureListActivity;

import static com.example.android.heedn.dummy.Constants.COUNT;

/**
 * Implementation of App Widget functionality.
 */
public class HEEDn_widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.heedn_widget);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int numberofScriptures = 0;
        if(prefs.contains(COUNT)) {
            numberofScriptures = prefs.getInt(COUNT, 0);
        }

        Intent intent = new Intent(context, ReviewScriptureActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);

        views.setTextViewText(R.id.appwidget_text, "You have "+ numberofScriptures + " HEEDn scriptures to review");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    public static void updateHEEDnWidgets(Context context, AppWidgetManager widgetManager,  int[] appWidgetIds) {
        for( int appWidgetId : appWidgetIds ){
            updateAppWidget(context, widgetManager, appWidgetId);
        }
    }


}

