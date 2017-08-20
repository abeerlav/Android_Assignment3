package a2dv606_aa223de.assignment3.Road_Map;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Abeer on 3/16/2017.
 */

public class Route_Map_Handler {
     private static HashMap <String,String> routeURL = new HashMap<String,String>();
    private static ArrayList<String> destinations;
    private static  ArrayList<String> coordinates;



    public static File readKMLfile(URL url, Context context) {
        try {
            URLConnection urlconnection = url.openConnection();
            urlconnection.connect();
            InputStream inputStream = urlconnection.getInputStream();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            FileOutputStream fileOutputStream = context.openFileOutput("route.xml", Context.MODE_PRIVATE);
            String line = null;
            while ((line = bufferReader.readLine()) != null) {
                fileOutputStream.write(line.getBytes());
            }
            fileOutputStream.close();
            bufferReader.close();
            inputStream.close();

        } catch (IOException e) {
            System.out.print("error in reading kml file");
        }
        return context.getFileStreamPath("route.xml");
    }

    public static void parseKmlFile(File file) throws IOException, XmlPullParserException {
             coordinates = new ArrayList<String>();
            FileInputStream myfileStream  =  new FileInputStream (file);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser   = factory.newPullParser();
                 parser.setInput(myfileStream,null);
                while (parser.getEventType()!= XmlResourceParser.END_DOCUMENT) {
                    String tag = parser.getName();
                    if (parser.getEventType() == XmlResourceParser.START_TAG) {

                        if (tag.equals("coordinates")) {
                            coordinates.add(parser.nextText());
                        }
                    }
                    parser.next();
                }
            }

       public static Route createRoute(){
           // get the start and the end coordinates
           String[] arr = coordinates.get(0).split(",");
           String startLng = arr[0];
           String startLat = arr[1];
           String endLat = arr[arr.length - 2];
           String endLng= arr[arr.length - 3];
           arr = endLng.split(" ");
           endLng = arr[arr.length-1];
           LatLng start = new LatLng(Double.parseDouble(startLat),Double.parseDouble(startLng));
           LatLng end = new LatLng(Double.parseDouble(endLat),Double.parseDouble(endLng));
           Route route = new Route(start,end);
          // get the intermediates
             arr= coordinates.get(0).split(" ");
           for (int i = 0; i < arr.length; i++) {
               String[] intermediates_arr= arr[i].split(",");
               String lat = intermediates_arr[1];
               String lng = intermediates_arr[0];
               route.getIntermediate().add(
                       new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
           }


      return route;
       }
    public static String getCityURL (String city){

     return routeURL.get(city);
    }

    public static void setup_routeList() {
        routeURL.put("Stockholm","http://cs.lnu.se/android/VaxjoToStockholm.kml");
        routeURL.put("Copenhagen","http://cs.lnu.se/android/VaxjoToCopenhagen.kml");
        routeURL.put("Odessa","http://cs.lnu.se/android/VaxjoToOdessa.kml");
    }
}


