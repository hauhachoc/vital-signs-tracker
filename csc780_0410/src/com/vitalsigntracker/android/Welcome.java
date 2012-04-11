package com.vitalsigntracker.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		case R.id.mnuAbout:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			return true;			
		case R.id.mnuExit:
			moveTaskToBack(true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	
}
