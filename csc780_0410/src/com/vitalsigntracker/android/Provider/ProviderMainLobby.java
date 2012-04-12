package com.vitalsigntracker.android.Provider;

import org.json.JSONException;
import org.json.JSONObject;

import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ProviderMainLobby extends Activity {

	PendingIntent pendingIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.providermainlobby);					
	}	
	
	public void subscribePatientOnClick (View v) {
		Intent i = new Intent(this, SubscribePatient.class);
		startActivity(i);
	}
	
	public void providerInboxOnClick (View v) {
		Toast.makeText(this, "inbox on click", Toast.LENGTH_SHORT).show();
	}
	
	public void displayTableOnClick (View v) {
		Intent i = new Intent(this, DisplayTable.class);
		startActivity(i);
	}
	
	public void displayChartOnClick (View v) {
		Intent i = new Intent(this, DisplayGraph.class);
		startActivity(i);		
	}
	
	public void signOutOnClick (View v) throws JSONException {
		
		String json = prepareJSONString();
    	String response = ConnectionManager.connect(json);    	
    	boolean success = false;    	       
		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");	
		
		if (success) {			
			Intent serviceIntent = new Intent (this, ProviderConnectionThread.class);
			serviceIntent.setData(Uri.parse("custom://" + 1));
			serviceIntent.setAction(String.valueOf(1));
			pendingIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);		
			alarmManager.cancel(pendingIntent);
			stopService(serviceIntent);			
			Intent i = new Intent(this, Welcome.class);
			startActivity(i);
			//Toast.makeText(this, "Stop Service", Toast.LENGTH_LONG).show();
		}
	}
	
	public String prepareJSONString() {
        String str = null;

        try {
            JSONObject object = new JSONObject();
            object.put("code", "providerlogout");                   
            str = object.toString();                        
        } catch (Exception e) {
            e.printStackTrace();  
        }
        return str;
    }
}
