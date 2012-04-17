package com.vitalsigntracker.android.Patient;

import org.json.JSONException;
import org.json.JSONObject;
import com.vitalsigntracker.android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;

public class PatientLogin extends Activity {
	
	private String MY_PREFS = "MY_PREFS";
	private SharedPreferences mySharedPreferences;
	
	private EditText username;
	private EditText password;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientlogin);
        
        username = (EditText) findViewById(R.id.usernameField);
        password = (EditText) findViewById(R.id.passwordField);
        
        username.setText("");
        password.setText("");        
    }
    
    //When user clicks the 'OK' button:
    //1. verify that username and password are correct.
    //2. if it's correct, switch to the main screen.
    //3. else, pop up dialog error message.
    public void okLoginClick(View v) throws JSONException {
    	
    	String json = prepareJSONString();
    	String response = ConnectionManager.connect(json);    	
    	boolean success = false;    	       
	
		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");			
		        
        if (success) {
        	
        	mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
    		SharedPreferences.Editor editor = mySharedPreferences.edit();
    		editor.putString("patientEmail", username.getText().toString());
    		editor.putString("patientname", obj.getString("patientname"));
    		editor.commit();
    		
        	Intent i = new Intent(this, PatientMainLobby.class);
        	startActivity(i);
        	
		} else {				
			
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Sign-In Failed");
			alertDialog.setMessage("Email & Password mismatch.");
			alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int i) {
					username.setText("");
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
            object.put("code", "patientLogin");
            object.put("email", username.getText());            
            object.put("password", password.getText());        
            str = object.toString();
        } catch (Exception e) {
            e.printStackTrace();  
        }
        return str;
    }   
    
    //Clear the username and password fields when the user click
    //'reset' button.
    public void resetClick(View v) {
    	username.setText("");
    	password.setText("");   	
    }
    
    public void clickForgetPassword(View v) {
    	//Source code here
    	Intent i = new Intent(this, PatientForgetPassword.class);
    	startActivity(i);  	    
    }
    
    public void clickActivate(View v) {
    	//Source code here
    	Intent j = new Intent(this, PatientActivateAccount.class);
    	startActivity(j);   	
    }
}