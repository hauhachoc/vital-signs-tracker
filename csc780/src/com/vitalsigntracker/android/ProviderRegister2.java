package com.vitalsigntracker.android;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

import org.json.JSONObject;

import metadata.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ProviderRegister2 extends Activity {

	EditText email;
	EditText phoneNumber;
	EditText password;

	private InputStream inpS;
	private OutputStream outS;

	String providerId, fName, lName;
	
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

	public void uiSubmitClick(View v) {	

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
		} catch (Exception e) {
			e.printStackTrace();
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
