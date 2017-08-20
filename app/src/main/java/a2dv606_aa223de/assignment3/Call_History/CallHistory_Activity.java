package a2dv606_aa223de.assignment3.Call_History;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import a2dv606_aa223de.assignment3.R;

public class CallHistory_Activity extends ListActivity {
    ArrayAdapter<Call> adapter;
    CallHistoryDataSource callHistoryDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_history);
      callHistoryDataSource = new CallHistoryDataSource(this);
        try {
            callHistoryDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Call> values = callHistoryDataSource.getAllCalls();

       adapter = new ArrayAdapter<Call>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Call");
        menu.add(0, v.getId(), 0, "SMS");
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adpapterinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
       Call call =  adapter.getItem(adpapterinfo.position);
        if(item.getTitle()=="Call"){
            makeCall(call);
        }
        else if(item.getTitle()=="SMS"){
            sendSMS(call);
        }else{
            return false;
        }
        return true;
    }

    private void sendSMS(Call call) {

        Intent msgIntent = new Intent(Intent.ACTION_SENDTO);
        msgIntent.setData(Uri.parse("smsto:"));
        msgIntent.putExtra("sms_body", call.getCall());

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager
                .queryIntentActivities(msgIntent, 0);

        if(activities.size() > 0){
            String title = "select app";
            Intent chooser = Intent.createChooser(msgIntent, title);
            startActivity(chooser);
        }
    }

    private void makeCall(Call call) {
        String number = "tel:"+call.getCall().toString();
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(number)));
    }
}

