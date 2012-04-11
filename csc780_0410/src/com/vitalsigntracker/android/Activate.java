package com.vitalsigntracker.android;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import metadata.Constants;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Activate extends Activity {

	private InputStream inpS;
    private OutputStream outS;
    
    EditText drname, email, password;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activate);
		
		drname = (EditText) findViewById(R.id.doctorNameField);
		email = (EditText) findViewById(R.id.yourEmailField);
		password = (EditText) findViewById(R.id.yourPasswordField);
	}
	
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
            JSONObject obj = new JSONObject(response);
            boolean success = obj.getBoolean("status");
            inpS.close();
            out.close();
            outS.close();
            sock.close();
            if (success) {
            	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("Activate Account Success");
				alertDialog.setMessage("Account Activated.");
				alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int i) {
						finish();											
					}
				});
				alertDialog.show();
            	
			} else {				
				
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("Activate Account Failed");
				alertDialog.setMessage("Information Incorrect.");
				alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int i) {
						drname.setText("");
						email.setText("");
						password.setText("");											
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
            object.put("code", "activateAccount");
            object.put("drname", drname.getText());
            object.put("email", email.getText());
            object.put("password", password.getText());        
            str = object.toString();
        } catch (Exception e) {
            e.printStackTrace();  
        }
        return str;
    }	
}
