package com.vitalsigntracker.android.Patient;

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

public class PatientChangeEmail extends Activity {
	
    private EditText email;
    private String MY_PREFS = "MY_PREFS";
	private SharedPreferences mySharedPreferences;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patientchangeemail);
		email = (EditText) findViewById(R.id.editText1);
	}

	public void submitEmailOnClick(View v) throws JSONException {
		String json = prepareJSONString();
    	String response = ConnectionManager.connect(json);    	
    	boolean success = false;    	       
	
		JSONObject obj = new JSONObject(response);
		success = obj.getBoolean("status");	
        if (success) {
        	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Modify Account Success");
			alertDialog.setMessage("Email Account Change.");
			
			mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
    		SharedPreferences.Editor editor = mySharedPreferences.edit();
    		editor.putString("patientEmail", email.getText().toString());	    		
    		editor.commit();
    		
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
        SharedPreferences mySharedPreferences = this.getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String patientEmail = mySharedPreferences.getString("patientEmail", "");
		
        try {
            JSONObject object = new JSONObject();
            object.put("code", "patientModifyAccount");
            object.put("subcode", "email");
            object.put("newemail", email.getText());
            object.put("oldemail", patientEmail);
            str = object.toString();
        } catch (Exception e) {
            e.printStackTrace();  
        }
        return str;
    }	
}
