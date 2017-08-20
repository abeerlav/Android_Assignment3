package a2dv606_aa223de.assignment3.Widget;

import android.app.Activity;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a2dv606_aa223de.assignment3.R;
/**
 * Created by Abeer on 3/8/2017.
 */


public class WidgetConfig extends ListActivity {
    private List<String> cities = new ArrayList<String>();
    private Map<String,String> cityAndUri = new HashMap<String,String>();
   private WidgetConfig context;
    AppWidgetManager appWidgetManager;
    RemoteViews remoteViews;
    int widgetId;
    private static final String PREFERENCE_NAME = "preference_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        context=this;
                /* Add cities to list */
        setup_cities_List();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_list, cities));
        Bundle extra =getIntent().getExtras();
        if(extra!=null){

            widgetId= extra.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);


        }
           appWidgetManager = AppWidgetManager.getInstance(context);
            remoteViews = new RemoteViews(this
                .getApplicationContext().getPackageName(), R.layout.widget_layout);
                /* Attach list item listener */
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClick());

    }

    /* Private Help Entities */
    private class OnItemClick implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		/* Find selected activity */
            String city = cities.get(position);
            String uri = cityAndUri.get(city);
            setToPreference(widgetId,city,uri);


            Intent intent = new Intent(context, UpdateWidgetService.class);
            intent.putExtra(UpdateWidgetService.SERVICE_INTENT, UpdateWidgetService.WIDGET_INIT);
            intent.putExtra("widget_id", widgetId);
            getApplicationContext().startService(intent);


            Intent resultValue= new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,widgetId);
            setResult(RESULT_OK,resultValue);
            finish();

        }
    }
    private void setup_cities_List() {


            addCities( "Alingsås",
                    "http://www.yr.no/place/Sweden/Västra_Götaland/Alingsås/forecast.xml");
   addCities("Alvesta",
                "http://www.yr.no/place/Sweden/Kronoberg/Alvesta/forecast.xml");
       addCities("Arboga",
                "http://www.yr.no/place/Sweden/Västmanland/Arboga/forecast.xml");
        addCities("Arvika",
                "http://www.yr.no/place/Sweden/Värmland/Arvika/forecast.xml");
     addCities("Avesta",
                "http://www.yr.no/place/Sweden/Dalarna/Avesta/forecast.xml");
       addCities("Boden",
                "http://www.yr.no/place/Sweden/Norrbotten/Boden/forecast.xml");
        addCities("Bollnäs",
                "http://www.yr.no/place/Sweden/Gävleborg/Bollnäs/forecast.xml");
    addCities("Borlänge",
                "http://www.yr.no/place/Sweden/Dalarna/Borlänge/forecast.xml");
       addCities("Borås",
                "http://www.yr.no/place/Sweden/Västra_Götaland/Borås/forecast.xml");
        addCities("Brunflo",
                "http://www.yr.no/place/Sweden/Jämtland/Brunflo~2718991/forecast.xml");
       addCities("Bålsta",
                "http://www.yr.no/place/Sweden/Uppsala/Bålsta/forecast.xml");
       addCities("Degerfors",
                "http://www.yr.no/place/Sweden/Örebro/Degerfors/forecast.xml");
       addCities("Enköping",
                "http://www.yr.no/place/Sweden/Uppsala/Enköping/forecast.xml");
        addCities("Eskilstuna",
                "http://www.yr.no/place/Sweden/Södermanland/Eskilstuna/forecast.xml");
        addCities("Eslöv",
                "http://www.yr.no/place/Sweden/Scania/Eslöv/forecast.xml");
        addCities("Fagersta",
                "http://www.yr.no/place/Sweden/Västmanland/Fagersta/forecast.xml");
        addCities("Falkenberg",
                "http://www.yr.no/place/Sweden/Halland/Falkenberg/forecast.xml");
        addCities("Falköping",
                "http://www.yr.no/place/Sweden/Västra_Götaland/Falköping/forecast.xml");
        addCities("Falun",
                "http://www.yr.no/place/Sweden/Dalarna/Falun/forecast.xml");
        addCities("Finspång",
                "http://www.yr.no/place/Sweden/Östergötland/Finspång/forecast.xml");
        addCities("Fårösund",
                "http://www.yr.no/place/Sweden/Gotland/Fårösund/forecast.xml");
        addCities("Gothenburg",
                "http://www.yr.no/place/Sweden/Västra_Götaland/Gothenburg/forecast.xml");
        addCities("Gällivare",
                "http://www.yr.no/place/Sweden/Norrbotten/Gällivare/forecast.xml");
        addCities("Gävle",
                "http://www.yr.no/place/Sweden/Gävleborg/Gävle/forecast.xml");
        addCities("Halmstad",
                "http://www.yr.no/place/Sweden/Halland/Halmstad/forecast.xml");
        addCities("Helsingborg",
                "http://www.yr.no/place/Sweden/Scania/Helsingborg/forecast.xml");
        addCities("Hemse",
                "http://www.yr.no/place/Sweden/Gotland/Hemse/forecast.xml");
        addCities("Holmsund",
                "http://www.yr.no/place/Sweden/Västerbotten/Holmsund~605676/forecast.xml");
        addCities("Hudiksvall",
                "http://www.yr.no/place/Sweden/Gävleborg/Hudiksvall/forecast.xml");
        addCities("Härnösand",
                "http://www.yr.no/place/Sweden/Västernorrland/Härnösand/forecast.xml");
        addCities("Hässleholm",
                "http://www.yr.no/place/Sweden/Scania/Hässleholm/forecast.xml");
        addCities("Höganäs",
                "http://www.yr.no/place/Sweden/Scania/Höganäs/forecast.xml");
        addCities("Jönköping",
                "http://www.yr.no/place/Sweden/Jönköping/Jönköping/forecast.xml");
        addCities("Kalmar",
                "http://www.yr.no/place/Sweden/Kalmar/Kalmar/forecast.xml");
        addCities("Karlshamn",
                "http://www.yr.no/place/Sweden/Blekinge/Karlshamn/forecast.xml");
        addCities("Karlskoga",
                "http://www.yr.no/place/Sweden/Örebro/Karlskoga/forecast.xml");
        addCities("Karlskrona",
                "http://www.yr.no/place/Sweden/Blekinge/Karlskrona/forecast.xml");
        addCities("Karlstad",
                "http://www.yr.no/place/Sweden/Värmland/Karlstad/forecast.xml");
        addCities("Katrineholm",
                "http://www.yr.no/place/Sweden/Södermanland/Katrineholm/forecast.xml");
        addCities("Kinna",
                "http://www.yr.no/place/Sweden/Västra_Götaland/Kinna/forecast.xml");
        addCities("Kiruna",
                "http://www.yr.no/place/Sweden/Norrbotten/Kiruna/forecast.xml");
        addCities("Klintehamn",
                "http://www.yr.no/place/Sweden/Gotland/Klintehamn/forecast.xml");
        addCities("Kristianstad",
                "http://www.yr.no/place/Sweden/Scania/Kristianstad/forecast.xml");
        addCities("Kristinehamn",
                "http://www.yr.no/place/Sweden/Värmland/Kristinehamn/forecast.xml");
        addCities("Krokom",
                "http://www.yr.no/place/Sweden/Jämtland/Krokom/forecast.xml");
        addCities("Kumla",
                "http://www.yr.no/place/Sweden/Örebro/Kumla/forecast.xml");
        addCities("Kungsbacka",
                "http://www.yr.no/place/Sweden/Halland/Kungsbacka/forecast.xml");
        addCities("Kungälv",
                "http://www.yr.no/place/Sweden/Västra_Götaland/Kungälv/forecast.xml");



        }

        private void addCities(String city, String uri) {
            cities.add(city);
            cityAndUri.put(city, uri);
        }
    public void setToPreference(int widgetId, String city,String uri) {
        SharedPreferences.Editor prefs = getBaseContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        prefs.putString(String.valueOf( widgetId)+"_city", city);
        prefs.putString(String.valueOf(widgetId)+ "_uri", uri);
        prefs.commit();
    }




    public static String getCityPreference(Context context,int widgetID) {
        SharedPreferences userDetails =context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String pref = userDetails.getString(String.valueOf(widgetID)+"_city",null);

        if(pref!=null){
            return pref;
        }
        return "";
    }

    public static String getUriPreference(Context context,int widgetID) {
        SharedPreferences userDetails =context .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String pref = userDetails.getString(String.valueOf(widgetID)+"_uri",null);

        if(pref!=null){
            return pref;
        }
        return "";
    }

    }
