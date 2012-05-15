package com.vitalsigntracker.android.Patient;

import metadata.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PatientActivateAccount extends Activity {

	EditText drname, email, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activate);

		drname = (EditText) findViewById(R.id.doctorNameField);
		email = (EditText) findViewById(R.id.yourEmailField);
		password = (EditText) findViewById(R.id.yourPasswordField);
	}

	public void okLoginClick(View v) throws JSONException {

		String json = prepareJSONString();
		String response = ConnectionManager.connect(json);
		boolean success = false;

		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");
		if (success) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Activate Account Success");
			alertDialog.setMessage("Account Activated.");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							finish();
						}
					});
			alertDialog.show();

		} else {

			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Activate Account Failed");
			alertDialog.setMessage("Information Incorrect.");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							drname.setText("");
							email.setText("");
							password.setText("");
						}
					});
			alertDialog.show();
		}
	}

	public String prepareJSONString() {
		String str = null;

		try {
			JSONObject object = new JSONObject();
			object.put("code", Constants.PATIENT_ACTIVATE_ACCOUNT);
			object.put("drname", drname.getText());
			object.put("email", email.getText());
			object.put("password", password.getText());
			str = object.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
