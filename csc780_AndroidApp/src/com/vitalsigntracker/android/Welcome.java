/* Welcome class is the main class of this application.
 * This class displays two options to the user to sign-in
 * to Vital Signs Tracker application: either as a health-
 * care provider (doctor) or a patient.
 * 
 * @author	Kelvin Komensen
 * @version	1.0.0 (CSC780 - SFSU Spring 2012)
 */
package com.vitalsigntracker.android;

import com.vitalsigntracker.android.Patient.PatientLogin;
import com.vitalsigntracker.android.Provider.ProviderLogin;
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

	/*
	 * This method starts a new intent when a patient clicks
	 * this button
	 */
	public void patientOnClick(View v) {
		Intent i = new Intent(this, PatientLogin.class);
		startActivity(i);
	}
	
	/*
	 * This method starts a new intent when a health-care
	 * provider (doctor) clicks this button.
	 */
	public void providerOnClick(View v) {
		Intent j = new Intent(this, ProviderLogin.class);
		startActivity(j);
	}

	/*
	 * This method avoids the user to return to the previous
	 * screen once he or she sign-out from the application.
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(this, Welcome.class);
		startActivity(i);
	}

	/* 
	 * This method displays menu when the user clicks on the 
	 * default Android menu button.
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options, menu);
		return true;
	}

	/*
	 * This method calls the right class when the user clicks
	 * on the menu options.
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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
