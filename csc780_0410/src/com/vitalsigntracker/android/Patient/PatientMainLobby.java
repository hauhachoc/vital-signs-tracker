package com.vitalsigntracker.android.Patient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.*;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PatientMainLobby extends Activity {

	String MY_PREFS = "MY_PREFS";
	SharedPreferences mySharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patientmainlobby);
	}

	public void modifyDataOnClick(View v) {
		// Toast.makeText(this, "click to edit user info",
		// Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, PatientModifyInfo.class);
		startActivity(i);
	}

	public void providerInfoOnClick(View v) {
		// Toast.makeText(this, "click for provider info",
		// Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, RequestProviderInfo.class);
		startActivity(i);
	}

	public void enterManuallyOnClick(View v) {
		// Toast.makeText(this, "click to enter manually",
		// Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, PatientEnterManually1.class);
		startActivity(i);
	}

	public void bluetoothDeviceOnClick(View v) {
		Toast.makeText(this, "click to listen to bluetooth device",
				Toast.LENGTH_SHORT).show();

	}

	public void displayReportOnClick(View v) {
		Intent i = new Intent(this, PatientDisplayTable.class);
		startActivity(i);
	}

	public void emergencyRequestOnClick(View v) throws JSONException {
		LocationManager lm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		LocationListener listener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
			}
		};
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);

		String locProvider = lm.getBestProvider(criteria, true);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
				listener);
		// lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
		// listener);

		Location curLocation = lm.getLastKnownLocation(locProvider);

		double currentLatitude = curLocation.getLatitude();
		double currentLongitude = curLocation.getLongitude();

		Geocoder mylocation = new Geocoder(getApplicationContext(),
				Locale.getDefault());
		List<Address> address = null;
		try {
			address = mylocation.getFromLocation(currentLatitude,
					currentLongitude, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Address add = address.get(0);
		mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
		String str = null;

		JSONObject json = new JSONObject();
		json.put("code", "patientemergencyrequest");
		json.put("latitude", currentLatitude);
		json.put("longitude", currentLongitude);
		json.put("streetName", add.getAddressLine(0));
		json.put("city", add.getLocality());
		json.put("patientname",
				mySharedPreferences.getString("patientname", ""));
		json.put("message", "Emergency Request!");
		str = json.toString();

		String response = ConnectionManager.connect(str);
		boolean success = false;

		Log.v("PatientMainLobby", "response " + response);

		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");

		if (success) {
			// Health care provider is online.
			Toast.makeText(this, "Emergency Request Submitted.",
					Toast.LENGTH_LONG).show();

		} else {
			Toast.makeText(this, "Provider is offline.", Toast.LENGTH_LONG)
					.show();
			// Health care provider is offline.
			String drphone = "1" + obj.getString("drphone");
			String msg = "Patient Name: "
					+ mySharedPreferences.getString("patientname", "")
					+ " Address: " + add.getAddressLine(0) + ", "
					+ add.getLocality() + " Message: Emergency Request!";
			SmsManager sm = SmsManager.getDefault();
			sm.sendTextMessage(drphone, null, msg, null, null);
			Toast.makeText(this, "Emergency Request Send via SMS.",
					Toast.LENGTH_LONG).show();
		}
	}

	public void signOutOnClick(View v) {
		Intent i = new Intent(this, Welcome.class);
		startActivity(i);
	}
}
