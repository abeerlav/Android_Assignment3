package a2dv606_aa223de.assignment3.Road_Map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Abeer on 3/15/2017.
 */

public class Route {
    private LatLng start;
    private LatLng end;
    private ArrayList<LatLng> intermediate;


    public Route(LatLng start , LatLng end) {
        this.start=start;
        this.end=end;
        intermediate = new ArrayList<LatLng>();
    }
    public Route(){

    }

    public LatLng getStart() {
        return start;
    }

    public void setStart(LatLng start) {
        this.start = start;
    }

    public ArrayList<LatLng> getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(ArrayList<LatLng> intermediate) {
        this.intermediate = intermediate;
    }

    public LatLng getEnd() {

        return end;
    }

    public void setEnd(LatLng end) {
        this.end = end;
    }
}
