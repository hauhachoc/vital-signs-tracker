package com.vitalsigntracker.android.Patient;

import org.json.JSONException;
import org.json.JSONObject;
import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PatientModifyInfo extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinformation);				
	}
	
	
	public void uiModifyEmailOnClick (View v) {
		//Toast.makeText(this, "modify email on click", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, PatientChangeEmail.class);
		startActivity(i);
	}
	
	public void uiModifyPhoneOnClick (View v) {
		//Toast.makeText(this, "modify phone on click", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, PatientChangePhone.class);
		startActivity(i);
	}
	
	public void uiModifyPasswordOnClick (View v) {
		//Toast.makeText(this, "modify password on click", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, PatientChangePassword.class);
		startActivity(i);
	}
	
	
	public void uiOkClick(View v) throws JSONException {
		
		String json = prepareJSONString();
    	String response = ConnectionManager.connect(json);    	    	  	       
	
    	JSONObject obj = new JSONObject(response);
    	boolean success = obj.getBoolean("status");
        
        if (success) {
        	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Modify Info Success");
			alertDialog.setMessage("Press 'Continue' to continue");
			alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
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
            object.put("code", "editUserInfo");                        
            str = object.toString();

        } catch (Exception e) {
            e.printStackTrace();  
        }
        return str;
    }
}
