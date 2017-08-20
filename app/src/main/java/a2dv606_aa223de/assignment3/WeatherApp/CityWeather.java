package a2dv606_aa223de.assignment3.WeatherApp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import a2dv606_aa223de.assignment3.R;
/**
 * Created by Abeer on 3/8/2017.
 */


public class CityWeather extends ListActivity {

	private InputStream input;
	private WeatherReport report = null;
	private WeatherAdapter adapter;
	private ListView listview;
	private List<WeatherForecast> Weatherlist;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView listview = (ListView) findViewById(android.R.id.list);
		TextView title=(TextView) findViewById(R.id.placeholder);
		Weatherlist = new ArrayList<WeatherForecast>();
		adapter = new WeatherAdapter(this, Weatherlist);
		listview.setAdapter(adapter);

		Intent intent = getIntent();
		String city = intent.getStringExtra("city");
		String uri = intent.getStringExtra("uri");
		title.setText( city);

		if(checkNetWorkConntection()) {

			URL url = null;
			try {
				url = new URL(uri);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			AsyncTask task = new WeatherRetriever().execute(url);
		}
		else {
			title.setText("No internet  available!");
		}








	}
	private boolean checkNetWorkConntection() {

		ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnectedOrConnecting();
	}



	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	//	inflater.inflate(R.menu.weather_menu, menu);
		return true;
	}


	private void printReportToLog() {
		if (this.report != null) {
            /*Print some meta data to the UI for the testing purposes*/
			TextView placeholder = (TextView) findViewById(R.id.placeholder);




        	/* Print forecasts */
			int count = 0;
			for (WeatherForecast forecast : report) {
				count++;
				Weatherlist.add(forecast);


				System.out.println("Forecast "+count);
				System.out.println( forecast.toString() );
			}
			adapter.notifyDataSetChanged();
		}
		else {
			System.out.print( "Weather report has not been loaded.");
		}
	}

	private class WeatherRetriever extends AsyncTask<URL, Void, WeatherReport> {
		protected WeatherReport doInBackground(URL... urls) {
			try {
				return WeatherHandler.getWeatherReport(urls[0]);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		protected void onProgressUpdate(Void... progress) {

		}

		protected void onPostExecute(WeatherReport result) {
			Toast.makeText(getApplicationContext(), "WeatherRetriever task finished", Toast.LENGTH_LONG).show();

			report = result;
			printReportToLog();
		}
	}


	class WeatherAdapter  extends ArrayAdapter<WeatherForecast> {

		public WeatherAdapter(Context context, List<WeatherForecast> weatherlist) {
			super(context, R.layout.weather_raw_view,weatherlist);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row;
			if (convertView == null) {	// Create new row view object
				LayoutInflater inflater = LayoutInflater.from(getContext());
				row = inflater.inflate(R.layout.weather_raw_view,parent,false);
			}
			else    // reuse old row view to save time/battery
				row = convertView;

	    		/* Add new data to row object */
			WeatherForecast weatherForecast = this.getItem(position);
			TextView date= (TextView)row.findViewById(R.id.city_date);
			TextView time= (TextView)row.findViewById(R.id.city_time);
			TextView rain= (TextView)row.findViewById(R.id.city_rain);
			TextView wind= (TextView)row.findViewById(R.id.city_wind);
			TextView temp= (TextView)row.findViewById(R.id.city_temp);
			ImageView icon = (ImageView)row.findViewById(R.id.weather_icon);
			date.setText("date: "+String.valueOf(weatherForecast.getStartYYMMDD()));
			time.setText("time: "+String.valueOf(weatherForecast.getStartHHMM() + " _ " + weatherForecast.getEndHHMM()));
			rain.setText("rain: "+String.valueOf(weatherForecast.getRain() + " mm/h"));
			wind.setText("wind: "+ String.valueOf(weatherForecast.getWindDirection()+ " " + weatherForecast.getWindSpeed() + " m/s"));
		    temp.setText(String.valueOf(weatherForecast.getTemperature()+" °"));
			icon.setImageResource(getResources().getIdentifier("drawable/icon_"
					+weatherForecast.getWeatherCode(), null,getPackageName()));


			return row;
		}
	}
}