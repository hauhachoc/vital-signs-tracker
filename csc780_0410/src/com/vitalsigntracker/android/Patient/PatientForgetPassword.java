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

public class PatientForgetPassword extends Activity {

	EditText pName, email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgetpassword);

		pName = (EditText) findViewById(R.id.forgetnameField);
		email = (EditText) findViewById(R.id.forgetEmailField);
	}

	public void forgetOkClick(View v) throws JSONException {

		String json = prepareJSONString();
		String response = ConnectionManager.connect(json);
		boolean success = false;

		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");

		if (success) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Retrieve Password Success");
			alertDialog.setMessage("Your password is " + obj.getString("password"));
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							finish();
						}
					});
			alertDialog.show();

		} else {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Retrieve Password Failed");
			alertDialog.setMessage("Name & Email Incorrect.");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							pName.setText("");
							email.setText("");
						}
					});
			alertDialog.show();
		}
	}

	public void forgetResetClick(View v) {
		pName.setText("");
		email.setText("");
	}

	public String prepareJSONString() {
		String str = null;

		try {
			JSONObject object = new JSONObject();
			object.put("code", Constants.PATIENT_FORGET_PASSWORD);
			object.put("pname", pName.getText());
			object.put("email", email.getText());
			str = object.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
