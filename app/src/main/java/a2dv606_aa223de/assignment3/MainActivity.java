package a2dv606_aa223de.assignment3;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a2dv606_aa223de.assignment3.Call_History.CallHistory_Activity;
import a2dv606_aa223de.assignment3.City_Map.MapsActivity;
import a2dv606_aa223de.assignment3.Road_Map.Road_Map;
/**
 * Created by Abeer on 3/8/2017.
 */


public class MainActivity extends ListActivity {


    private List<String> activities = new ArrayList<String>();
    private Map<String,Class> name2class = new HashMap<String,Class>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Add Activities to list */
        setup_activities();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_main, activities));

        /* Attach list item listener */
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClick());
    }



    /* Private Help Entities */
    private class OnItemClick implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		/* Find selected activity */
            String activity_name = activities.get(position);
            Class activity_class = name2class.get(activity_name);

    		/* Start new Activity */
            Intent intent = new Intent(MainActivity.this,activity_class);
            MainActivity.this.startActivity(intent);
        }
    }

    /* Diagnostics while developing */
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setup_activities() {
       addActivity("Call History ", CallHistory_Activity.class);
        addActivity("City Map",MapsActivity.class);
        addActivity("The Road Map ",Road_Map.class);

    }

    private void addActivity(String name, Class activity) {
        activities.add(name);
        name2class.put(name, activity);
    }


}
