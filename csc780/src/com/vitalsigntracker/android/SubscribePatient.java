package com.vitalsigntracker.android;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

import metadata.Constants;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SubscribePatient extends Activity {

	EditText patientName, email, phone;
	private InputStream inpS;
	private OutputStream outS;
	
	public String MY_PREFS = "MY_PREFS";
	public SharedPreferences mySharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscribepatient);
		
		patientName = (EditText) findViewById(R.id.sNameField);
		email = (EditText) findViewById(R.id.semailField);
		phone = (EditText) findViewById(R.id.sphoneField);
	}
	
	public void uiSubscribeClick(View v) {
		
		if ( patientName.getText() == (null) ||				
			email.getText() == null || 
			phone.getText() == null) {
			
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Uncomplete Information");
			alertDialog.setMessage("Please enter all information.");
			alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int i) {
					
				}
			});
			alertDialog.show();
			
		} else {
			mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
			String str = null;
			try {
				JSONObject object = new JSONObject();
				object.put("code", "subscribePatient");
				object.put("patientName", patientName.getText());
				object.put("email", email.getText());
				object.put("phone", phone.getText());
				object.put("providerid", mySharedPreferences.getString("providerid", ""));
				str = object.toString();			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
	            SocketAddress socketAddress = new InetSocketAddress(Constants.IP_ADD, Constants.PORT);
	            Socket sock = new Socket();
	            sock.connect(socketAddress, 10 * 1000);
	            inpS = sock.getInputStream();
	            outS = sock.getOutputStream();
	            	           
	            Scanner in = new Scanner(inpS);
	            PrintWriter out = new PrintWriter(outS, true);
	            	            
	            out.println(str);
	            
	            String response = in.nextLine();
	            JSONObject obj = new JSONObject(response);
	            boolean success = obj.getBoolean("status");
	            
	            if (success) {
	            	
	            	AlertDialog alertDialog = new AlertDialog.Builder(this)
					.create();
	            	alertDialog.setTitle("Message");
	            	alertDialog.setMessage("Registration Success");
	            	alertDialog.setButton("Continue",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface d, int i) {
							patientName.setText("");
							email.setText("");
							phone.setText("");
						}
					});
	            	alertDialog.show();
	            	
	            	Intent i = new Intent(this, ProviderMainLobby.class);
	            	startActivity(i);
					
				} else {

					AlertDialog alertDialog = new AlertDialog.Builder(this)
							.create();
					alertDialog.setTitle("Registration Failed");
					alertDialog.setMessage("Provider ID has been used.");
					alertDialog.setButton("Continue",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface d, int i) {
							
								}
							});
					alertDialog.show();
				}
				                                   
	            inpS.close();
	            out.close();
	            outS.close();
	            sock.close();	            
	            
			} catch (Exception e) {
				e.printStackTrace();
			}	            	
		}
	}	
	
	public void uiResetClick(View v) {	
		patientName.setText("");
		email.setText("");
		phone.setText("");
	}
}
