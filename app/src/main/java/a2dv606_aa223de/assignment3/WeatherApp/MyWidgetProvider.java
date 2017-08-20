package a2dv606_aa223de.assignment3.WeatherApp;

/**
 * Created by Abeer on 3/8/2017.
 */

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import a2dv606_aa223de.assignment3.R;

public class MyWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i = 0; i < appWidgetIds.length; i++) {
            int widget_id = appWidgetIds[i];
            Intent updateIntent = new Intent(context, UpdateWidgetService.class);
            updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            updateIntent.putExtra(UpdateWidgetService.SERVICE_INTENT, UpdateWidgetService.WIDGET_UPDATE);
            updateIntent.putExtra("widget_id",widget_id);
            context.startService(updateIntent);
            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // set on click reload button
            Intent clickIntent = new Intent(context, MyWidgetProvider.class);
            clickIntent .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{widget_id});
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, widget_id, clickIntent, 0);
            view.setOnClickPendingIntent(R.id.weather_button, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds, view);
        }
    }








}





