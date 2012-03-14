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

import metadata.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPassword extends Activity {

	EditText fName,
			 lName;
	
	private InputStream inpS;
    private OutputStream outS;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgetpassword);
		
		fName = (EditText) findViewById(R.id.forgetfirstnameField);
		lName = (EditText) findViewById(R.id.forgetlastnameField);
	}
	
	public void forgetOkClick(View v) {
		
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
            
            if (response.length() != 0) {
            	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("Retrieve Password Success");
				alertDialog.setMessage("Your password is " + response);
				alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int i) {
						finish();						
					}
				});
				alertDialog.show();			
				
			} else {								
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle("Retrieve Password Failed");
				alertDialog.setMessage("First & Last Name Mismatch.");
				alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface d, int i) {
						fName.setText("");
						lName.setText("");
						fName.setSelection(0);						
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

	public void forgetResetClick(View v) {
		fName.setText("");
		lName.setText("");
	}
	
	public String prepareJSONString() {
        String str = null;

        try {
            JSONObject object = new JSONObject();
            object.put("code", "forgetPassword");
            object.put("firstname", fName.getText());            
            object.put("lastname", lName.getText());        
            str = object.toString();
            
//For debug purpose only
            Toast.makeText(this, "JSON Objects are: " + str, Toast.LENGTH_LONG).show();

            
        } catch (Exception e) {
            e.printStackTrace();  
        }
        return str;
    }
}
