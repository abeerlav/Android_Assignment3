package a2dv606_aa223de.assignment3.Widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import a2dv606_aa223de.assignment3.R;

/**
 * Created by Abeer on 3/8/2017.
 */

public class UpdateWidgetService   extends Service{
        public static String SERVICE_INTENT = "service_intent";
        public static final int WIDGET_INIT = 0;
        public static final int WIDGET_UPDATE = 1;
        private WeatherReport report;



        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            System.out.println("on start command");
            int command = intent.getIntExtra(SERVICE_INTENT, -1);
            if (intent == null) {
                return START_STICKY;
            }

            switch (command){
                case  WIDGET_INIT: {
                    int widget_id = intent.getIntExtra("widget_id", -1);
                    int[] widgetIds = {widget_id};
                Intent widget_intent = new Intent(getApplicationContext(), MyWidgetProvider.class);
                widget_intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                widget_intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
                sendBroadcast(widget_intent);
                    break;}

                case WIDGET_UPDATE: {
                int appWidgetId = intent.getIntExtra("widget_id", -1);

                if (isNetworkAvailable()) {
                    String city = WidgetConfig.getCityPreference(this, appWidgetId);
                    String uri=    WidgetConfig.getUriPreference(this,appWidgetId);

                    try {
                        URL Url = new URL(uri);

                        new WeatherRetriever(appWidgetId, city,uri).execute(Url);

                    } catch (MalformedURLException e) {

                        e.printStackTrace();
                    }
                } else {
                   System.out.println("no internet!");
                }
                    break;
            }}
            stopSelf();
            return START_STICKY;


        }




private class WeatherRetriever extends AsyncTask<URL, Void, WeatherReport> {
    private int widgetId;
    private String widgetCity;
    private String widgetUri;
    RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout);

    private WeatherRetriever(int id, String city, String uri) {
        widgetId = id;
        widgetCity = city;
        widgetUri= uri;
    }

    protected WeatherReport doInBackground(URL... urls) {

        try {
            return WeatherHandler.getWeatherReport(urls[0]);


        } catch (Exception e) {
            throw new RuntimeException();
        }


    }

    protected void onProgressUpdate(Void... progress) {

    }

    protected void onPostExecute(WeatherReport result) {
        report = result;

        if (report != null) {
            WeatherForecast   weatherForecast = report.getForecasts().get(0);
          setupViews(weatherForecast);
            setupOnWidgetClick();
        }


    }

    private void setupOnWidgetClick() {
        Intent intent = new Intent(UpdateWidgetService.this, CityWeather.class);
        intent.putExtra("city", widgetCity);
        intent.putExtra("uri",widgetUri);
        PendingIntent pendingIntent = PendingIntent.getActivity(UpdateWidgetService.this, widgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.layout, pendingIntent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(UpdateWidgetService.this);
        appWidgetManager.updateAppWidget(widgetId, views);
    }

    private void setupViews(WeatherForecast forecast) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String time = formatter.format(new Date(calendar.getTimeInMillis()));
        views.setTextViewText(R.id.city,widgetCity);
        views.setTextViewText(R.id.time,time);
        views.setTextViewText(R.id.update_temp, forecast.getTemperature() + " °" );
        views.setTextViewText(R.id.date, forecast.getStartYYMMDD());
        views.setTextViewText(R.id.type, forecast.getWeatherName());
        views.setImageViewResource(R.id.weather_icon,getResources().getIdentifier("drawable/icon_"
                +forecast.getWeatherCode(), null,getPackageName()));
    }


}





    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;

        }

        return isAvailable;

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}