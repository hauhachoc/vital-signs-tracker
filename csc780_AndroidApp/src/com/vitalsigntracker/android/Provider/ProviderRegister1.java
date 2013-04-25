/*
 * ProviderRegister1: UI for the health care provider
 * (doctor) to create a new account (register).
 * Once the doctor successfully create an account,
 * he/she can use this application in order to
 * subscribe his/her patient to the system.
 */
package com.vitalsigntracker.android.Provider;
import com.vitalsigntracker.android.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProviderRegister1 extends Activity {

	EditText providerId;
	EditText fName;
	EditText lName;
	Button next;

	public String MY_PREFS = "MY_PREFS";
	public SharedPreferences mySharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.provider_register1);

		providerId = (EditText) findViewById(R.id.uiprovideridField);
		fName = (EditText) findViewById(R.id.uiproviderfirstnameField);
		lName = (EditText) findViewById(R.id.uiproviderlastnameField);
	}

	//click next button to go to the next page to enter additional info.
	//it uses SharedPreferences object to store and carry the info. to the
	//next page.	
	public void nextButtonOnClick(View v) {

		mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putString("pid", providerId.getText().toString());
		editor.putString("f_name", fName.getText().toString());
		editor.putString("l_name", lName.getText().toString());
		editor.commit();
		Intent i = new Intent(this, ProviderRegister2.class);
		startActivity(i);
	}

	public void resetClick(View v) {
		providerId.setText("");
		fName.setText("");
		lName.setText("");
	}
}
