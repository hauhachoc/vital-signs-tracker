/*
 * PatientChangePhone will be called when the patient
 * wants to change his phone number.
 * New phone number will be sent to server, and
 * database will be updated.
 */
package com.vitalsigntracker.android.Patient;
import metadata.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PatientChangePhone extends Activity {

	private EditText phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patientchangephone);
		phone = (EditText) findViewById(R.id.editText1);
	}

	/*
	 * submitPhoneOnClick will be called when the patient clicks
	 * on the submit button. New phone number will be sent
	 * to server. Database will be updated.
	 * @exception	JSONException
	 */
	public void submitPhoneOnClick(View v) throws JSONException {
		String json = prepareJSONString();
		String response = ConnectionManager.connect(json);
		boolean success = false;

		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");
		if (success) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Modify Phone Success");
			alertDialog.setMessage("Phone Number Change.");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							finish();
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
		SharedPreferences mySharedPreferences = this.getSharedPreferences(
				"MY_PREFS", MODE_PRIVATE);
		String patientEmail = mySharedPreferences.getString("patientEmail", "");

		try {
			JSONObject object = new JSONObject();
			object.put("code", Constants.PATIENT_MODIFY_ACCOUNT);
			object.put("subcode", "phone");
			object.put("newphone", phone.getText());
			object.put("oldemail", patientEmail);
			str = object.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
