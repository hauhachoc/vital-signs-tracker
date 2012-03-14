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
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import metadata.Constants;

public class LoginPage extends Activity {
	
	private EditText username;
	private EditText password;
	
	private InputStream inpS;
    private OutputStream outS;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        username = (EditText) findViewById(R.id.usernameField);
        password = (EditText) findViewById(R.id.passwordField);
        
        username.setText("");
        password.setText("");
        
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
            Toast.makeText(this, "BUtton OK Press", Toast.LENGTH_LONG).show();
           
            Scanner in = new Scanner(inpS);
            PrintWriter out = new PrintWriter(outS, true);
            
            String json = prepareJSONString();
            out.println(json);
            
            String response = in.nextLine();
//for debug purpose only
            Toast.makeText(this, "server response " + response, Toast.LENGTH_LONG).show();
                                    
            inpS.close();
            out.close();
            outS.close();
            sock.close();
            if (response.equals("Success")) {
            	Intent i = new Intent(this, MainLobby.class);
            	startActivity(i);
            	
			} else {				
				
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("Sign-In Failed");
				alertDialog.setMessage("Email & Password mismatch.");
				alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int i) {
						username.setText("");
						password.setText("");
						username.setSelection(0);						
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
            object.put("code", "login");
            object.put("email", username.getText());            
            object.put("password", password.getText());        
            str = object.toString();
            
//For debug purpose only
            Toast.makeText(this, "JSON Objects are: " + str, Toast.LENGTH_LONG).show();

            
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
    	Intent i = new Intent(this, ForgetPassword.class);
    	startActivity(i);
    	
    	
    }
    
    public void clickJoin(View v) {
    	//Source code here
    	Intent j = new Intent(this, Register.class);
    	startActivity(j);
    	
    }
}