package com.vitalsigntracker.android.Provider;

import org.json.JSONException;
import org.json.JSONObject;

import com.vitalsigntracker.android.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class ProviderConnectionThread extends Service {
	
	private String MY_PREFS = "MY_PREFS";
	private SharedPreferences mySharedPreferences;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		String json = prepareJSONString();
    	String response = ConnectionManager.connect(json);    	
    	boolean success = false;    	       
	
		JSONObject obj = null;
		try {
			obj = new JSONObject(response);
			success = obj.getBoolean("status");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        if (success) {
        	//Toast.makeText(this, "ProviderConnectionThread Success", Toast.LENGTH_LONG).show();
        	
        	//store the emergency information to SharedPreferences
        	mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
    		SharedPreferences.Editor editor = mySharedPreferences.edit();
    		try {
	    		editor.putFloat("emergencylatitude", (float) obj.getDouble("latitude"));
	    		editor.putFloat("emergencylongitude", (float) obj.getDouble("longitude"));
	    		editor.putString("emergencymessage", obj.getString("message"));	    		
	    		editor.putString("emergencypatientname", obj.getString("patientname"));
	    		editor.putString("emergencystreetname", obj.getString("streetName"));
	    		editor.putString("emergencycity", obj.getString("city"));
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		editor.commit();
        	
        	NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        	int icon = R.drawable.ic_launcher;
        	String tickerText = "Notification";
        	long when = System.currentTimeMillis();
        	
        	Notification notification = new Notification(icon, tickerText, when);
        	notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        	Context context = getApplicationContext();
        	String alertMessage = "Emergency Alert";
        	String alertTitle = "Emergency Notification";
        	Intent i = new Intent(this, EmergencyNotification.class);
        	PendingIntent launchIntent = PendingIntent.getActivity(context, 0, i, 0);
        	notification.setLatestEventInfo(context, alertTitle, alertMessage, launchIntent);
        	notification.defaults = Notification.DEFAULT_ALL;
        	nm.notify(999, notification);
        	
        } else {
        	//Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
        }            		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub		
		stopSelf();
		//Toast.makeText(this, "Background service destroyed", Toast.LENGTH_LONG).show();
		super.onDestroy();		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//Toast.makeText(this, "Background service created", Toast.LENGTH_LONG).show();
	}	
	
	public String prepareJSONString() {
        String str = null;

        try {
            JSONObject object = new JSONObject();
            object.put("code", "checkAlertMessage");
            object.put("check", "test");        
            str = object.toString();                      

        } catch (Exception e) {
            e.printStackTrace();  
        }
        return str;
    }
}
