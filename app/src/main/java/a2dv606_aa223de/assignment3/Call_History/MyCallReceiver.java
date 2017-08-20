package a2dv606_aa223de.assignment3.Call_History;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by Abeer on 3/7/2017.
 */

public class MyCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
   if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
       String incomingCall = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
       Toast.makeText(context,"call from "+incomingCall,Toast.LENGTH_LONG).show();
       Call call= null;

           CallHistoryDataSource callDataSource = new CallHistoryDataSource(context);

       try {
           callDataSource.open();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       call =callDataSource.createCall(incomingCall);
           callDataSource.close();
       Toast.makeText(context,"added " +call.getCall(),Toast.LENGTH_LONG).show();

   }
    }
}
