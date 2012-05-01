package com.vitalsigntracker.android.Provider;

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

	// take care the alert message (json object) and trigger notification
	// manager.
	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String json = b.getString("json");
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

	// method activates the notification manager, extracts the GeoPoint and
	// infos.
	public void triggerNotification(JSONObject obj) {
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
		PendingIntent launchIntent = PendingIntent
				.getActivity(context, 0, i, 0);
		notification.setLatestEventInfo(context, alertTitle, alertMessage,
				launchIntent);
		notification.defaults = Notification.DEFAULT_ALL;
		nm.notify(999, notification);

		/*
		 * isProviderOnline = true; notifyingThread = new Thread(bgTask,
		 * "CHECK EMERGENCY ALERT"); notifyingThread.start();
		 */
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		super.onStartCommand(intent, flags, startId);
		Log.v(LOG_TAG, "starting thread");
		isProviderOnline = true;
		notifyingThread = new Thread(bgTask, "CHECK EMERGENCY ALERT");
		notifyingThread.start();
		return START_STICKY;
	}

	private Runnable bgTask = new Runnable() {

		@Override
		public void run() {
			// Thread keeps running as long as the provider is signing-in.
			while (isProviderOnline) {
				String str = null;
				JSONObject json = new JSONObject();
				try {
					json.put("code", "checkAlertMessage");
					str = json.toString();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				String response = ConnectionManager.connect(str);
				boolean success = false;
				try {
					JSONObject obj = new JSONObject(response);
					success = obj.getBoolean("status");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// there is an alert message on queue.
				if (success) {
					Log.v(LOG_TAG, "Connect with client");
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("json", response);
					msg.setData(b);
					handler.sendMessage(msg);
					// isProviderOnline = false;
				}
			}
		}
	};

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
		// Toast.makeText(this, "Background service created",
		// Toast.LENGTH_LONG).show();
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
