package com.vitalsigntracker.android.Provider;

import org.json.JSONException;
import org.json.JSONObject;

import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ProviderRegister2 extends Activity {

	private EditText email;
	private EditText phoneNumber;
	private EditText password;
	private String providerId, fName, lName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.provider_register2);

		email = (EditText) findViewById(R.id.uiemailField);
		phoneNumber = (EditText) findViewById(R.id.uiphoneField);
		password = (EditText) findViewById(R.id.uiproviderpasswordField);				
				
		SharedPreferences mySharedPreferences = this.getSharedPreferences("MY_PREFS", MODE_PRIVATE);

		providerId = mySharedPreferences.getString("pid", "");
		fName = mySharedPreferences.getString("f_name", "");
		lName = mySharedPreferences.getString("l_name", "");		
	}

	public void uiResetClick(View v) {
		email.setText("");
		phoneNumber.setText("");
		password.setText("");
	}

	public void uiSubmitClick(View v) throws JSONException {	

		String json = prepareJSONString();
    	String response = ConnectionManager.connect(json);    	
    	boolean success = false;    	       
		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");	
		
		if (success) {
			Intent j = new Intent(this, ProviderLogin.class);
			startActivity(j);
		} else {

			AlertDialog alertDialog = new AlertDialog.Builder(this)
					.create();
			alertDialog.setTitle("Registration Failed");
			alertDialog.setMessage("Provider ID has been used.");
			alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							finish();
						}
					});
			alertDialog.show();
		}		
	}

	public String prepareJSONString() {
		String str = null;
		
		try {
			JSONObject object = new JSONObject();
			object.put("code", "providerRegister");
			object.put("providerId", providerId);
			object.put("fName", fName);
			object.put("lName", lName);
			object.put("email", email.getText());
			object.put("phoneNumber", phoneNumber.getText());
			object.put("password", password.getText());
			str = object.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
