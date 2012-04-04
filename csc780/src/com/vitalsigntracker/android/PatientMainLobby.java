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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class PatientMainLobby extends Activity {
	
	public String MY_PREFS = "MY_PREFS";
	private InputStream inpS;
    private OutputStream outS;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientmainlobby);
	}
	
	public void modifyDataOnClick(View v) {
		Toast.makeText(this, "click to edit user info", Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(this, ModifyUserInfo.class);
		startActivity(i);
		
	}
	
	public void providerInfoOnClick(View v) {
		Toast.makeText(this, "click for provider info", Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(this, RequestProviderInfo.class);
		startActivity(i);
		
	}
	
	public void enterManuallyOnClick(View v) {
		Toast.makeText(this, "click to enter manually", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, PatientEnterManually1.class);
		startActivity(i);
	}
	
	public void bluetoothDeviceOnClick(View v) {
		Toast.makeText(this, "click to listen to bluetooth device", Toast.LENGTH_SHORT).show();
		
	}
	
	public void displayReportOnClick(View v) {					
		
		Intent i = new Intent(this, PatientDisplayTable.class);
		startActivity(i);
	}
	
	public void emergencyOnClick (View v) {
		
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
            boolean success = obj.getBoolean("status");
          
            if (success) {
            	Toast.makeText(this, "Message Has Been Sent to Health Provider", Toast.LENGTH_LONG).show();
			}         
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }	
	}
	
	public void signOutOnClick(View v) {		
		Intent i = new Intent(this, Welcome.class);
		startActivity(i);
	}
	
	public String prepareJSONString() {
        String str = null;
        SharedPreferences mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);		
		
        try {
            JSONObject object = new JSONObject();
            object.put("code", "emergencyrequest");
            object.put("patientemail", mySharedPreferences.getString("patientEmail", ""));
            object.put("message", "I NEED HELP!");
            str = object.toString();         

        } catch (Exception e) {
            e.printStackTrace();  
        }
        return str;
    }
	
}
