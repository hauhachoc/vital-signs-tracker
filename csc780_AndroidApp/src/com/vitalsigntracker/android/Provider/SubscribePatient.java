/*
 * SubscribePatient provides UI for the health care
 * provider (doctor) to subscribe a new patient to the
 * system. The new patient's info. will be sent and 
 * stored in the server.
 */
package com.vitalsigntracker.android.Provider;
import metadata.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SubscribePatient extends Activity {

	private EditText patientName, email, phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscribepatient);

		patientName = (EditText) findViewById(R.id.sNameField);
		email = (EditText) findViewById(R.id.semailField);
		phone = (EditText) findViewById(R.id.sphoneField);
	}

	/*
	 * This method send the new patient info. to server.
	 * It will pop up alert message if the info. is not complete.
	 * It will receive a respond from server either success or
	 * failed.
	 */
	public void uiSubscribeClick(View v) throws JSONException {

		if (patientName.getText() == (null) || email.getText() == null
				|| phone.getText() == null) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Uncomplete Information");
			alertDialog.setMessage("Please enter all information.");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
						}
					});
			alertDialog.show();
			
		} else {
			String str = null;
			try {
				JSONObject object = new JSONObject();
				object.put("code", Constants.PROVIDER_SUBSCRIBE_PATIENT);
				object.put("patientName", patientName.getText());
				object.put("email", email.getText());
				object.put("phone", phone.getText());
				object.put("providerid", ProviderLogin.providerId);
				str = object.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}

			String response = ConnectionManager.connect(str);
			boolean success = false;
			JSONObject obj = new JSONObject(response);
			success = obj.getBoolean("status");
			if (success) {

				AlertDialog alertDialog = new AlertDialog.Builder(this)
						.create();
				alertDialog.setTitle("Message");
				alertDialog.setMessage("Registration Success");
				alertDialog.setButton("Continue",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface d, int i) {
								patientName.setText("");
								email.setText("");
								phone.setText("");
							}
						});
				alertDialog.show();

				Intent i = new Intent(this, ProviderMainLobby.class);
				startActivity(i);

			} else {

				AlertDialog alertDialog = new AlertDialog.Builder(this)
						.create();
				alertDialog.setTitle("Registration Failed");
				alertDialog.setMessage("Provider ID has been used.");
				alertDialog.setButton("Continue",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface d, int i) {

							}
						});
				alertDialog.show();
			}
		}
	}

	public void uiResetClick(View v) {
		patientName.setText("");
		email.setText("");
		phone.setText("");
	}
}
