package com.vitalsigntracker.android;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

import metadata.Constants;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

public class ProviderConnectionThread extends Service {

	private InputStream inpS;
	private OutputStream outS;
	private String MY_PREFS = "MY_PREFS";
	private SharedPreferences mySharedPreferences;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		try {
            SocketAddress socketAddress = new InetSocketAddress(Constants.IP_ADD, Constants.PORT);
            Socket sock = new Socket();
            sock.connect(socketAddress, 10 * 1000);
            inpS = sock.getInputStream();
            outS = sock.getOutputStream();                       
           
            Scanner in = new Scanner(inpS);
            PrintWriter out = new PrintWriter(outS, true);
            
            String json = prepareJSONString();            
            out.println(json);
            
            String response = in.nextLine();            
            inpS.close();
            out.close();
            outS.close();
            sock.close();
            
            JSONObject obj = new JSONObject(response);
            boolean success = obj.getBoolean("status");
            if (success) {
            	//Toast.makeText(this, "ProviderConnectionThread Success", Toast.LENGTH_LONG).show();
            	
            	//store the emergency information to SharedPreferences
            	mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
	    		SharedPreferences.Editor editor = mySharedPreferences.edit();
	    		editor.putFloat("emergencylatitude", (float) obj.getDouble("latitude"));
	    		editor.putFloat("emergencylongitude", (float) obj.getDouble("longitude"));
	    		editor.putString("emergencymessage", obj.getString("message"));	    		
	    		editor.putString("emergencypatientname", obj.getString("patientname"));
	    		editor.putString("emergencystreetname", obj.getString("streetName"));
	    		editor.putString("emergencycity", obj.getString("city"));
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
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		super.onStartCommand(intent, flags, startId);
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
