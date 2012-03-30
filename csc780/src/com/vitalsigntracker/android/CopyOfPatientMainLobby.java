package com.vitalsigntracker.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class CopyOfPatientMainLobby extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlobby);
	}
	
	public void userInfoOnClick(View v) {
		Toast.makeText(this, "click to edit user info", Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(this, ModifyUserInfo.class);
		startActivity(i);
		
	}
	
	public void providerOnClick(View v) {
		Toast.makeText(this, "click to edit provider info", Toast.LENGTH_SHORT).show();
		
	}
	
	public void editAppOnClick(View v) {
		Toast.makeText(this, "click to edit app.", Toast.LENGTH_SHORT).show();
		
	}
	
	public void generateReportOnClick(View v) {
		Toast.makeText(this, "click to generate report", Toast.LENGTH_SHORT).show();
		
	}
	
	public void generateChartOnClick(View v) {
		Toast.makeText(this, "click to generate chart", Toast.LENGTH_SHORT).show();
		
	}
	
	public void enterDataOnClick(View v) {
		Toast.makeText(this, "click to enter data", Toast.LENGTH_SHORT).show();
		
	}
	
	public void signOutOnClick(View v) {
		Toast.makeText(this, "click to sign out", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, Welcome.class);
		startActivity(i);
	}
	
	
}
