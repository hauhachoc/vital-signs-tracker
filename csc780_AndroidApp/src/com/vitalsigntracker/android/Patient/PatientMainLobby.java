/*
 * PatientMainLobby is the UI that provides the user
 * for several options, such as request change info.,
 * alert, enter vital signs info. manually, display
 * table, see provider information, start the blue-tooth
 * listening, and sign-out.
 * @author	Kelvin Komensen
 * @version	1.0.0 (CSC780 - SFSU Spring 2012) 
 */
package com.vitalsigntracker.android.Patient;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import metadata.Constants;

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

	//Patient can modify his/her email, phone number, and password
	public void modifyDataOnClick(View v) {		
		Intent i = new Intent(this, PatientModifyInfo.class);
		startActivity(i);
	}

	//Patient can request the complete info. of the health care provider.
	public void providerInfoOnClick(View v) {		
		Intent i = new Intent(this, RequestProviderInfo.class);
		startActivity(i);
	}
	
	//Patient can enter the vital signs info. manually
	public void enterManuallyOnClick(View v) {
		Intent i = new Intent(this, PatientEnterManually1.class);
		startActivity(i);
	}

	//Patient can active the bluetooth to listen to bluetooth device.
	public void bluetoothDeviceOnClick(View v) {
		/*Toast.makeText(this, "click to listen to bluetooth device",
				Toast.LENGTH_SHORT).show();*/
		Intent i = new Intent(this,BlueToothData.class);
		startActivity(i);

	}
	
	//Patient can check the report (table) weekly or monthly.
	//The response of this request is the table of vital signs info.
	//(periodically).
	public void displayReportOnClick(View v) {
		Intent i = new Intent(this, PatientDisplayTable.class);
		startActivity(i);
	}

	/* Patient request emergency alert manually.
	 * This method will send the user geolocation information to server.
	 * If the doctor is not online, server will send back 'failed' status;
	 * then this method will also send a text message to the doctor's
	 * phone.
	 */
	public void emergencyRequestOnClick(View v) throws JSONException {
		LocationManager lm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		LocationListener listener = new LocationListener() {
			
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
			}

			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
			}

			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
			}

			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
			}
		};
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);

		String locProvider = lm.getBestProvider(criteria, true);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
				listener);		
		Location curLocation = lm.getLastKnownLocation(locProvider);

		//get latitude and longitude coordinates
		double currentLatitude = curLocation.getLatitude();
		double currentLongitude = curLocation.getLongitude();

		//get the actual address from the latitude and longitude coordinates.
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

		//Put data in JSON string.
		JSONObject json = new JSONObject();
		json.put("code", Constants.PATIENT_EMERGENCY_REQUEST);
		json.put("latitude", currentLatitude);
		json.put("longitude", currentLongitude);
		json.put("streetName", add.getAddressLine(0));
		json.put("city", add.getLocality());
		json.put("patientname",
				mySharedPreferences.getString("patientname", ""));
		json.put("message", "Emergency Request!");
		str = json.toString();

		//send request to server.
		String response = ConnectionManager.connect(str);
		boolean success = false;

		Log.v("PatientMainLobby", "response " + response);

		//get response from server
		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");
		
		//Emergency request is sent to the doctor via server.
		//Doctor is online.
		if (success) {
			// Health care provider is online.
			Toast.makeText(this, "Emergency Request Submitted.",
					Toast.LENGTH_LONG).show();
		} 
		//Emergency request is sent to the doctor via SMS.
		//Doctor is offline.
		else {
			Toast.makeText(this, "Provider is offline.", Toast.LENGTH_LONG)
					.show();			
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

	/*
	 * The patient sign out from the application.
	 */
	public void signOutOnClick(View v) {
		Intent i = new Intent(this, Welcome.class);
		startActivity(i);
	}
}
