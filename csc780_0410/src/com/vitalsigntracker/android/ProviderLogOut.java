package com.vitalsigntracker.android;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ProviderLogOut extends Activity {

	PendingIntent pendingIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.providerlogout);
	}

	public void logOutOnClick(View v) {
		
		//this.stopService(new Intent(this, ProviderConnectionThread.class));
		Intent serviceIntent = new Intent (this, ProviderConnectionThread.class);
		serviceIntent.setData(Uri.parse("custom://" + 1));
		serviceIntent.setAction(String.valueOf(1));
		pendingIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		/*Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 5);*/
		//alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5*1000, pendingIntent);
		alarmManager.cancel(pendingIntent);
		
		Toast.makeText(this, "Stop Service", Toast.LENGTH_LONG).show();

		Intent i = new Intent(this, Welcome.class);
		startActivity(i);
	}
}
