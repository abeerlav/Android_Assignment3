package a2dv606_aa223de.assignment3.Road_Map;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import a2dv606_aa223de.assignment3.R;

public class Road_Map extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road__map);
        Route_Map_Handler.setup_routeList();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if(isNetworkAvailable()) {
        String strCityUrl= Route_Map_Handler.getCityURL("Stockholm");
            try {

                URL url = new URL(strCityUrl);
                new RouteDrawer().execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else{
            Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Copenhagen:
                try {
                    new RouteDrawer().execute(new URL (Route_Map_Handler.getCityURL("Copenhagen")));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.Stockholm:
                try {
                    new RouteDrawer().execute(new URL (Route_Map_Handler.getCityURL("Stockholm")));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.Odessa:
                try {
                    new RouteDrawer().execute(new URL (Route_Map_Handler.getCityURL("Odessa")));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                 break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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

    private class RouteDrawer extends AsyncTask<URL, Void, File> {
        private Route route ;
        @Override
        protected File doInBackground(URL... url) {
            return Route_Map_Handler.readKMLfile(url[0],getApplicationContext());
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {

            try {
                Route_Map_Handler.parseKmlFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
                route= Route_Map_Handler.createRoute();
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(route.getStart()));
                mMap.addMarker(new MarkerOptions().position(route.getEnd()));
                mMap.addPolyline(new PolylineOptions().addAll(route.getIntermediate()).width(5).color(Color.BLUE));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(route.getStart());
                for (LatLng lat : route.getIntermediate())
                     builder.include(lat);
                builder.include(route.getEnd());
                final LatLngBounds bounds = builder.build();
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.12);
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                mMap.moveCamera(cu);
                mMap.getUiSettings().setZoomControlsEnabled(true);



        }


    }}

}
