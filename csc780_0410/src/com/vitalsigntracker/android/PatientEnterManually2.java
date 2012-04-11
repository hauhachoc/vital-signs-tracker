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
import android.widget.Toast;

public class PatientEnterManually2 extends Activity {

	String MY_PREFS = "MY_PREFS";
	SharedPreferences mySharedPreferences;
	EditText systole, diastole, heartRate, bodyTemp;
	
	private InputStream inpS;
	private OutputStream outS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patiententermanually2);
		
		systole = (EditText) findViewById(R.id.systole);
		diastole = (EditText) findViewById(R.id.diastole);
		heartRate = (EditText) findViewById(R.id.heartRate);
		bodyTemp = (EditText) findViewById(R.id.bodyTemp);
		
	}

	public void submitButtonOnClick (View v) {
		
		if ((systole.getText()).equals(null) || 
				(diastole.getText()).equals(null) || 
				(heartRate.getText()).equals(null) ||
				(bodyTemp.getText()).equals(null)) {
			
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Error");
			alertDialog.setMessage("Please enter complete information.");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int i) {
															
				}
			});
			alertDialog.show();
			
		} else {
			
			try {
				SocketAddress socketAddress = new InetSocketAddress(
						Constants.IP_ADD, Constants.PORT);
				Socket sock = new Socket();
				sock.connect(socketAddress, 10 * 1000);
				inpS = sock.getInputStream();
				outS = sock.getOutputStream();
				
				Scanner in = new Scanner(inpS);
				PrintWriter out = new PrintWriter(outS, true);

				// get the json string
				String json = prepareJSONString();
				// send the json string to server
				out.println(json);

				String response = in.nextLine();
				JSONObject obj = new JSONObject(response);
	            boolean success = obj.getBoolean("status");
	            
				inpS.close();
				out.close();
				outS.close();
				sock.close();

				if (success) {
					
					Toast.makeText(this, "Data had been submitted.", Toast.LENGTH_LONG).show();
					Intent j = new Intent(this, PatientMainLobby.class);
					startActivity(j);
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String prepareJSONString() {
		String str = null;
		
		mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);						
		
		try {
			JSONObject object = new JSONObject();
			object.put("code", "patientEnterManually");
			object.put("patientname", mySharedPreferences.getString("patientname", ""));
			object.put("arm", mySharedPreferences.getString("arm", ""));
			object.put("position", mySharedPreferences.getString("position", ""));
			object.put("systole", Integer.parseInt(systole.getText().toString()));
			object.put("diastole", Integer.parseInt(diastole.getText().toString()));
			object.put("heartRate", Integer.parseInt(heartRate.getText().toString()));
			object.put("bodyTemp", Integer.parseInt(bodyTemp.getText().toString()));
			str = object.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
}
