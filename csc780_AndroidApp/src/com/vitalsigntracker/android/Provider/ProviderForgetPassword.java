/*
 * ProviderForgetPassword provides UI for the health care
 * provider (doctor) to retrieve his/her password.
 * Doctor needs to provide his/her first and last name; then
 * server will verify the info.
 * @author	Kelvin Komensen
 * @version	1.0.0 (CSC780 - SFSU Spring 2012) 
 */
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

	/*
	 * forgetOkClick method will be called when the user clicks
	 * on the submit button to send first & last name to the server.
	 * Server will validate those info. and return the result.
	 */
	public void forgetOkClick(View v) throws JSONException {
		String json = prepareJSONString();
		String response = ConnectionManager.connect(json);
		boolean success = false;

		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");

		//successfully retrieve password.
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
			//fail to retrieve password. wrong information.
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

	//clean up the first & last name fields (security issue).
	public void forgetResetClick(View v) {
		fName.setText("");
		lName.setText("");
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
