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
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ModifyUserInfo extends Activity {
	
	private InputStream inpS;
	private OutputStream outS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinformation);				
	}
	
	
	public void uiModifyEmailOnClick (View v) {
		Toast.makeText(this, "modify email on click", Toast.LENGTH_SHORT).show();
	}
	
	public void uiModifyPhoneOnClick (View v) {
		Toast.makeText(this, "modify phone on click", Toast.LENGTH_SHORT).show();
	}
	
	public void uiModifyPasswordOnClick (View v) {
		Toast.makeText(this, "modify password on click", Toast.LENGTH_SHORT).show();
	}
	
	
	public void uiOkClick(View v) {
		
		//Toast.makeText(this, "BUtton OK Press", Toast.LENGTH_LONG).show();
		try {
            SocketAddress socketAddress = new InetSocketAddress(Constants.IP_ADD, Constants.PORT);
            Socket sock = new Socket();
            sock.connect(socketAddress, 10 * 1000);
            inpS = sock.getInputStream();
            outS = sock.getOutputStream();
            //Toast.makeText(this, "BUtton OK Press", Toast.LENGTH_LONG).show();
            Scanner in = new Scanner(inpS);
            PrintWriter out = new PrintWriter(outS, true);
            
            String json = prepareJSONString();
            out.println(json);
            
            String response = in.nextLine();
//for debug purpose only
            //Toast.makeText(this, "server response " + response, Toast.LENGTH_LONG).show();
                                    
            inpS.close();
            out.close();
            outS.close();
            sock.close();

            //debug
            System.out.println("Response return value ; " + response);
            
            
            if (response.equals("Success")) {
            	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("Modify Info Success");
				alertDialog.setMessage("Press 'Continue' to continue");
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

        try {
            JSONObject object = new JSONObject();
            object.put("code", "editUserInfo");                        
            str = object.toString();
            
//For debug purpose only
            Toast.makeText(this, "JSON Objects are: " + str, Toast.LENGTH_LONG).show();
            
        } catch (Exception e) {
            e.printStackTrace();  
        }
        return str;
    }
}
