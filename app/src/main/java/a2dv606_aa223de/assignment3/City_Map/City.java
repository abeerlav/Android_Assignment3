package a2dv606_aa223de.assignment3.City_Map;


/**
 * Created by Abeer on 3/14/2017.
 */

public class City {
    private double lat;
    private double lng;
    private String city;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLat(double lat) {

        this.lat = lat;
    }

    public City(double lat,double lng,String city) {
        this.lat = lat;
        this.lng=lng;
        this.city=city;
    }
}