/*
 * PatientModifyInfo provides UI for the patient to modify
 * his/her basic information.
 * There are three options: change email, phone, and password.
 */
package com.vitalsigntracker.android.Patient;
import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PatientModifyInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinformation);
	}

	//Patient changes his/her email account.
	public void uiModifyEmailOnClick(View v) {		
		Intent i = new Intent(this, PatientChangeEmail.class);
		startActivity(i);
	}

	//Patient changes his/her phone number.
	public void uiModifyPhoneOnClick(View v) {
		Intent i = new Intent(this, PatientChangePhone.class);
		startActivity(i);
	}

	//Patient changes his/her password	
	public void uiModifyPasswordOnClick(View v) {
		Intent i = new Intent(this, PatientChangePassword.class);
		startActivity(i);
	}	
}
