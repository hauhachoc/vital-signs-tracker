package com.vitalsigntracker.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;

public class LoginPage extends Activity {
	
	private EditText username;
	private EditText password;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        username = (EditText) findViewById(R.id.usernameField);
        password = (EditText) findViewById(R.id.passwordField);
        
    }
    
    //When user clicks the 'OK' button:
    //1. verify that username and password are correct.
    //2. if it's correct, switch to the main screen.
    //3. else, pop up dialog error message.
    public void okLoginClick(View v) {
    	//Source code here
    	
    }
    
    
    //Clear the username and password fields when the user click
    //'reset' button.
    public void resetClick(View v) {
    	username.setText("");
    	password.setText("");
    	
    }
    
    public void clickForgetPassword(View v) {
    	//Source code here
    	Intent i = new Intent(this, ErrorSignIn.class);
    	startActivity(i);
    	
    	
    }
    
    public void clickJoin(View v) {
    	//Source code here
    	
    }
}