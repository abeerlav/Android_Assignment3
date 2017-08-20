package a2dv606_aa223de.assignment3.City_Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import a2dv606_aa223de.assignment3.City_Map.City;
import a2dv606_aa223de.assignment3.R;
/**
 * Created by Abeer on 5/8/2017.
 */


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<City> cities = new ArrayList<City>();
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    Marker crosshair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         parseCites(this);
        if(googleServicesAvailable()){
            setContentView(R.layout.activity_maps);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    }
    public void parseCites(Context context) {

        try {
            InputStream inputstream = context.getResources().openRawResource(R.raw.mycites);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
            String line;
            while((line=reader.readLine())!=null){
                String [] arr = line.split(":");
                String  city = arr[0];
                String  latitude  =arr[1] ;
                String  longitude = arr[2];
              City myCity = new City(Double.parseDouble(latitude),Double.parseDouble(longitude),city);
                cities.add(myCity);
            }

            reader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "cannot connect to google services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i =0; i<cities.size();i++)
        {goToLocation(cities.get(i).getLat(),cities.get(i).getLng(),cities.get(i).getCity());}

        final LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(cu);

       setListener();
    }

    private void setListener() {
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if(crosshair!=null)
                    crosshair.remove();

                crosshair = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer))
                        .anchor(0.5f, 0.5f)
                        .position(mMap.getCameraPosition().target));
            }}
        );
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Marker nearestMarker = null;
                double lowestDistance = Double.MAX_VALUE;

                if (markers != null) {
                    for (Marker marker : markers) {
                        double dist =   getDistance(marker.getPosition().latitude,marker.getPosition().longitude);
                        if (dist < lowestDistance) {
                            nearestMarker = marker;
                            lowestDistance = dist;
                        }
                    }

                DecimalFormat df = new DecimalFormat("#.00");
                String distanceString = df.format(lowestDistance);
                Toast.makeText(getApplicationContext(), "The distance to "+nearestMarker.getTitle()+" is "+
                        distanceString+"Km",Toast.LENGTH_SHORT).show();

            }}
        });
    }

    private double getDistance(Double lat, Double lang) {
       LatLng center = mMap.getCameraPosition().target;
           Double lat_center = center.latitude;
          Double lng_center = center.longitude;

       int R = 6371; // km
       double x = (lang - lng_center) * Math.cos((lat_center + lat) / 2);
       double y = (lat - lat_center);
       double distance = Math.sqrt(x * x + y * y) * R;
       return distance;}


    private void goToLocation(double lat, double lng, final String city) {
        LatLng s = new LatLng(lat,lng);
        builder.include(s);
       markers.add( mMap.addMarker(new MarkerOptions().position(s).title(city)));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                    if(marker.getTitle()!=null)
                    {    Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;}
                else
                return false;
                }


        });

    }
}
