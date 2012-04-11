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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class RequestProviderInfo extends Activity {

	public String MY_PREFS = "MY_PREFS";
	private InputStream inpS;
    private OutputStream outS;
    
    TextView drfname, drlname, dremail, drphone;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.requestproviderinfo);
		
		drfname = (TextView) findViewById(R.id.firstNameField);
		drlname = (TextView) findViewById(R.id.lastNameField);
		dremail = (TextView) findViewById(R.id.emailField); 
		drphone = (TextView) findViewById(R.id.phoneField);			
		
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
            	drfname.setText(obj.getString("drfname"));
            	drlname.setText(obj.getString("drlname"));
            	dremail.setText(obj.getString("dremail"));
            	drphone.setText(obj.getString("drphone"));
			}         
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }	
	}    	
  
	public String prepareJSONString() {
        String str = null;
        SharedPreferences mySharedPreferences = this.getSharedPreferences(MY_PREFS, MODE_PRIVATE);		
		
        try {
            JSONObject object = new JSONObject();
            object.put("code", "requestproviderinfo");
            object.put("patientemail", mySharedPreferences.getString("patientEmail", ""));                            
            str = object.toString();         

        } catch (Exception e) {
            e.printStackTrace();  
        }
        return str;
    }
}
	
	

