/*
 * ProviderLogin provides UI for the doctor to sign-in
 * to the application.
 * UI for doctor to: sign-in, retrieve password, and
 * create account/register.
 * If the doctor successfully sign-in to the system, this
 * class will also trigger the service to open a persistence
 * connection to server for the 'push notification'.
 */
package com.vitalsigntracker.android.Provider;
import metadata.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.util.Log;
import android.view.View;

public class ProviderLogin extends Activity {

	private static final String LOG_TAG = "ProviderLogin";

	private String MY_PREFS = "MY_PREFS";
	private SharedPreferences mySharedPreferences;
	private EditText username;
	private EditText password;
	public static String providerId = null;
	PendingIntent pendingIntent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.providerlogin);

		username = (EditText) findViewById(R.id.usernameField);
		password = (EditText) findViewById(R.id.passwordField);
	}

	// When user clicks the 'OK' button:
	// 1. verify that username and password are correct.
	// 2. if it's correct, switch to the main screen.
	// 3. else, pop up dialog error message.
	public void okLoginClick(View v) throws JSONException {
		String json = prepareJSONString();
		String response = ConnectionManager.connect(json);		
		JSONObject obj = new JSONObject(response);
		boolean success = obj.getBoolean("status");

		if (success) {
			//sign-in success
			mySharedPreferences = this.getSharedPreferences(MY_PREFS,
					MODE_PRIVATE);
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putString("allPatientName", response);
			editor.putString("providerid", providerId);
			editor.commit();
			
			//start a service in the background to open a persistence
			//connection between client and server.
			//this persistence socket connection will be used for 
			//alert message from the patient (push notification from server).
			Intent serviceIntent = new Intent(this,
					ProviderConnectionThread.class);
			serviceIntent.setData(Uri.parse("custom://" + 1));
			serviceIntent.setAction(String.valueOf(1));
			pendingIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
			try {
				pendingIntent.send();
			} catch (CanceledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.v(LOG_TAG, "ProviderLogin.java start service ...");

			Intent i = new Intent(this, ProviderMainLobby.class);
			startActivity(i);

		} else {
			//sign-in failed. pop up alert message.
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Sign-In Failed");
			alertDialog.setMessage("Email & Password mismatch.");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							username.setText("");
							password.setText("");							
						}
					});
			alertDialog.show();
		}
	}
	
	/*
	 * prepareJSONString creates a JSON String object.
	 * @param	None
	 * @return	str (JSON String)
	 */
	public String prepareJSONString() {
		String str = null;

		try {
			JSONObject object = new JSONObject();
			object.put("code", Constants.PROVIDER_LOGIN);
			object.put("providerid", username.getText());
			object.put("password", password.getText());
			str = object.toString();

			// store the providerid
			setProviderId(username.getText().toString());
			username.setText("");
			password.setText("");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	// Clear the username and password fields when the user click
	// 'reset' button.
	public void resetClick(View v) {
		username.setText("");
		password.setText("");
	}

	//when the doctor click on forget password button
	public void clickForgetPassword(View v) {
		Intent i = new Intent(this, ProviderForgetPassword.class);
		startActivity(i);
	}

	//when the doctor click on the register button.
	public void clickRegister(View v) {
		Intent j = new Intent(this, ProviderRegister1.class);
		startActivity(j);
	}

	public void setProviderId(String str) {
		providerId = str;
	}

	public static String getProviderId() {
		return providerId;
	}
}