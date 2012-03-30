package com.vitalsigntracker.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class Welcome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
				
	}
	
	public void patientOnClick(View v) {
	
		Intent i = new Intent(this, PatientLogin.class);
		startActivity(i);
	}
	
	public void providerOnClick(View v) {
		
		Intent j = new Intent(this, ProviderLogin.class);
		startActivity(j);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(this, Welcome.class);
		startActivity(i);
	}
	
	public boolean onCreateOptionsMenu (Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate()
	}
}
