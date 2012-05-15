package com.vitalsigntracker.android.Provider;

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

public class ProviderForgetPassword extends Activity {

	private EditText fName, lName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.providerforgetpassword);

		fName = (EditText) findViewById(R.id.forgetfirstnameField);
		lName = (EditText) findViewById(R.id.forgetlastnameField);
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
			alertDialog.setMessage("First & Last Name Mismatch.");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							fName.setText("");
							lName.setText("");
							fName.setSelection(0);
						}
					});
			alertDialog.show();
		}
	}

	public void forgetResetClick(View v) {
		fName.setText("");
		lName.setText("");
	}

	public String prepareJSONString() {
		String str = null;

		try {
			JSONObject object = new JSONObject();
			object.put("code", Constants.PROVIDER_FORGET_PASSWORD);
			object.put("firstname", fName.getText());
			object.put("lastname", lName.getText());
			str = object.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
