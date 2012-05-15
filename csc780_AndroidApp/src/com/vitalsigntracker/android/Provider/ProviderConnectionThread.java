/*
 * ProviderConnectionThread creates a service in the background.
 * This background service will create a new thread to open 
 * a persistence socket connection to server. This persistence
 * client-server connection will be used for 'push notification'
 * from server.
 * This class will be called once the doctor (health care provider)
 * sign-in to the system. This class (bg service) will be terminated
 * when the health care provider sign-out from the system.
 * If this class receive an 'emergency notification' from server,
 * then it will trigger an android notification on the top of menu bar.
 * The PendingIntent object will be called once the doctor click the
 * message on the notification bar to check the emergency request.
 * @author	Kelvin Komensen
 * @version	1.0.0 (CSC780 - SFSU Spring 2012)
 */
package com.vitalsigntracker.android.Provider;
import metadata.Constants;

import org.json.*;
import com.vitalsigntracker.android.*;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class ProviderConnectionThread extends Service {

	private static final String LOG_TAG = "ProviderConnectionThread";
	protected Thread notifyingThread;
	private String MY_PREFS = "MY_PREFS";
	private SharedPreferences mySharedPreferences;
	private boolean isProviderOnline;

	/*
	 * This method will create a new thread to open a persistence
	 * socket connection between client and server.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.v(LOG_TAG, "starting thread");
		isProviderOnline = true;
		notifyingThread = new Thread(bgTask, "CHECK EMERGENCY ALERT");
		notifyingThread.start();
		return START_STICKY;
	}

	/*
	 * This is the runnable that creates a new thread to open the
	 * persistence socket connection.
	 */
	private Runnable bgTask = new Runnable() {		
		public void run() {
			// Thread keeps running as long as the provider is signing-in.
			while (isProviderOnline) {
				String str = null;
				JSONObject json = new JSONObject();
				try {
					json.put("code", Constants.PROVIDER_CHECK_ALERT_MESSAGE);
					str = json.toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				//Open socket connection.
				String response = ConnectionManager.connect(str);
				boolean success = false;
				try {
					JSONObject obj = new JSONObject(response);
					success = obj.getBoolean("status");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				//An alert message (push notification) from server.
				//Put the JSON string object to bundle object and
				//trigger the Handler object to handle the notification.
				if (success) {
					Log.v(LOG_TAG, "Connect with client");
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("json", response);
					msg.setData(b);
					handler.sendMessage(msg);	
				}
			}
		}
	};
	
	//Handler object will extract the JSON object and
	//activate the notification.
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Log.v(LOG_TAG, "handleMessage called");
			Bundle b = msg.getData();
			String json = b.getString("json");
			Log.v(LOG_TAG, "json = " + json);
			JSONObject obj = null;
			String code = null;
			try {
				obj = new JSONObject(json);
				code = obj.getString("code");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (code.equals("patientemergencyrequest")) {
				ProviderConnectionThread.this.triggerNotification(obj);
			}
		}
	};
	
    /*
	 * This method activates the notification manager, extracts the GeoPoint and
	 * alert message information.
	 * The notification manager will be displayed on Android notification bar.
	 * We need PendingIntent object, so when the user click on the notification
	 * message, it will called the correct class to display the message & map.
	 */
	public void triggerNotification(JSONObject obj) {
		//store all emergency notification info. in SharedPreferences object.
		mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		try {
			editor.putFloat("emergencylatitude",
					(float) obj.getDouble("latitude"));
			editor.putFloat("emergencylongitude",
					(float) obj.getDouble("longitude"));
			editor.putString("emergencymessage", obj.getString("message"));
			editor.putString("emergencypatientname",
					obj.getString("patientname"));
			editor.putString("emergencystreetname", obj.getString("streetName"));
			editor.putString("emergencycity", obj.getString("city"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		editor.commit();
		
		//Create a notification manager object.
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;
		String tickerText = "Notification";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.flags = Notification.DEFAULT_LIGHTS
				| Notification.FLAG_AUTO_CANCEL;
		Context context = getApplicationContext();
		String alertMessage = "Emergency Alert";
		String alertTitle = "Emergency Notification";
		Intent i = new Intent(this, EmergencyNotification.class);
	
		//PendingIntent object will call EmergencyNotification class.
		PendingIntent launchIntent = PendingIntent
				.getActivity(context, 0, i, 0);
		notification.setLatestEventInfo(context, alertTitle, alertMessage,
				launchIntent);
		notification.defaults = Notification.DEFAULT_ALL;
		nm.notify(999, notification);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		isProviderOnline = false;
		notifyingThread.interrupt();
		Log.v(LOG_TAG, "thread stopped");
		stopSelf();
		super.onDestroy();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();		
	}

	public String prepareJSONString() {
		String str = null;

		try {
			JSONObject object = new JSONObject();
			object.put("status", "true");
			str = object.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
