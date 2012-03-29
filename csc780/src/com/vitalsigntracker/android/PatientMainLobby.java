package com.vitalsigntracker.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class PatientMainLobby extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientmainlobby);
	}
	
	public void modifyDataOnClick(View v) {
		Toast.makeText(this, "click to edit user info", Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(this, ModifyUserInfo.class);
		startActivity(i);
		
	}
	
	public void providerInfoOnClick(View v) {
		Toast.makeText(this, "click for provider info", Toast.LENGTH_SHORT).show();
		
	}
	
	public void enterManuallyOnClick(View v) {
		Toast.makeText(this, "click to enter manually", Toast.LENGTH_SHORT).show();
		
	}
	
	public void bluetoothDeviceOnClick(View v) {
		Toast.makeText(this, "click to listen to bluetooth device", Toast.LENGTH_SHORT).show();
		
	}
	
	public void displayReportOnClick(View v) {		
		Intent i = new Intent(this, PatientDisplayTable.class);
		startActivity(i);
	}
	
	public void signOutOnClick(View v) {		
		Intent i = new Intent(this, Welcome.class);
		startActivity(i);
	}
	
	
}
