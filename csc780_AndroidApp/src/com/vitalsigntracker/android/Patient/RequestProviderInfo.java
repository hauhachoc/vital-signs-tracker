/* RequestProviderInfo will retrieve the information
 * of the health care provider, such as phone number,
 * email, doctor's name, et cetera.
 * @author	Kelvin Komensen
 * @version	1.0.0 (CSC780 - SFSU Spring 2012) 
 */
package com.vitalsigntracker.android.Patient;
import metadata.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class RequestProviderInfo extends Activity {

	private String MY_PREFS = "MY_PREFS";
	private TextView drfname, drlname, dremail, drphone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.requestproviderinfo);

		drfname = (TextView) findViewById(R.id.firstNameField);
		drlname = (TextView) findViewById(R.id.lastNameField);
		dremail = (TextView) findViewById(R.id.emailField);
		drphone = (TextView) findViewById(R.id.phoneField);

		//send request to server.
		String json = prepareJSONString();
		String response = ConnectionManager.connect(json);
		boolean success = false;
		
		//get response from server.
		JSONObject obj = null;
		try {
			obj = new JSONObject(response);
			success = obj.getBoolean("status");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (success) {
			try {
				drfname.setText(obj.getString("drfname"));
				drlname.setText(obj.getString("drlname"));
				dremail.setText(obj.getString("dremail"));
				drphone.setText(obj.getString("drphone"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * prepareJSONString method creates a JSON String object.
	 * The JSON String object will be sent to server.
	 * @param	None
	 * @return	str (JSON String)
	 */
	public String prepareJSONString() {
		String str = null;
		SharedPreferences mySharedPreferences = this.getSharedPreferences(
				MY_PREFS, MODE_PRIVATE);

		try {
			JSONObject object = new JSONObject();
			object.put("code", Constants.PATIENT_REQUEST_PROVIDER_INFO);
			object.put("patientemail",
					mySharedPreferences.getString("patientEmail", ""));
			str = object.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
