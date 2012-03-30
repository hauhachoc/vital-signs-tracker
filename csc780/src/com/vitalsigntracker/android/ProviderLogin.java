package com.vitalsigntracker.android;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import metadata.Constants;

public class ProviderLogin extends Activity {
	
	public String MY_PREFS = "MY_PREFS";
	public SharedPreferences mySharedPreferences;
	
	private EditText username;
	private EditText password;
	
	private InputStream inpS;
    private OutputStream outS;
    
    public static String providerId = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.providerlogin);
        
        username = (EditText) findViewById(R.id.usernameField);
        password = (EditText) findViewById(R.id.passwordField);      
    }
    
    //When user clicks the 'OK' button:
    //1. verify that username and password are correct.
    //2. if it's correct, switch to the main screen.
    //3. else, pop up dialog error message.
    public void okLoginClick(View v) {
    	//Source code here
    	try {
            SocketAddress socketAddress = new InetSocketAddress(Constants.IP_ADD, Constants.PORT);
            Socket sock = new Socket();
            sock.connect(socketAddress, 10 * 1000);
            inpS = sock.getInputStream();
            outS = sock.getOutputStream();                       
           
            Scanner in = new Scanner(inpS);
            PrintWriter out = new PrintWriter(outS, true);
            
            String json = prepareJSONString();
            out.println(json);
            
            String response = in.nextLine();
            //Toast.makeText(this, "Json :" + response, Toast.LENGTH_LONG).show();
            inpS.close();
            out.close();
            outS.close();
            sock.close();
            
            JSONObject obj = new JSONObject(response);
            String status = obj.getString("status");
          
            if (status.equals("Success")) {
          
            	Toast.makeText(this, "status " + status, Toast.LENGTH_LONG).show();
            	
            	mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
	    		SharedPreferences.Editor editor = mySharedPreferences.edit();
	    		editor.putString("allPatientName", response);	    		
	    		editor.commit();	    		    
	    	            	
            	Intent i = new Intent(this, ProviderMainLobby.class);
            	startActivity(i);
            	
			} else {				
				
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("Sign-In Failed");
				alertDialog.setMessage("Email & Password mismatch.");
				alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int i) {
						username.setText("");
						password.setText("");
						//username.setSelection(0);						
					}
				});
				alertDialog.show();
			}
            
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
    	
  
	public String prepareJSONString() {
        String str = null;

        try {
            JSONObject object = new JSONObject();
            object.put("code", "providerlogin");
            object.put("providerid", username.getText());            
            object.put("password", password.getText());        
            str = object.toString();
            
            //store the providerid
            setProviderId(username.getText().toString());            
            username.setText("");
            password.setText("");

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
    	Intent i = new Intent(this, ProviderForgetPassword.class);
    	startActivity(i);  	    
    }
    
    public void clickRegister(View v) {
    	//Source code here
    	Intent j = new Intent(this, ProviderRegister1.class);
    	startActivity(j);
    }
    
    public void setProviderId(String str) {
    	providerId = str;
    }
    
    public static String getProviderId() {
    	return providerId;
    }       
}