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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PatientChangePassword extends Activity {
	private InputStream inpS;
    private OutputStream outS;
    EditText password;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patientchangepassword);
		password = (EditText) findViewById(R.id.editText1);
	}

	public void submitPasswordOnClick(View v) {
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
				alertDialog.setTitle("Modify Password Success");
				alertDialog.setMessage("Password Change.");
				alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int i) {
						finish();											
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
        SharedPreferences mySharedPreferences = this.getSharedPreferences("MY_PREFS", MODE_PRIVATE);
        String patientEmail = mySharedPreferences.getString("patientEmail", "");
		
        try {
            JSONObject object = new JSONObject();
            object.put("code", "patientModifyAccount");
            object.put("subcode", "password");
            object.put("newpassword", password.getText());
            object.put("oldemail", patientEmail);
            str = object.toString();
        } catch (Exception e) {
            e.printStackTrace();  
        }
        return str;
    }	
}
