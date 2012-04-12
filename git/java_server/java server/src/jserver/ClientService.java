/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import jserver.sqlclasses.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author kelvin
 */
public class ClientService implements Runnable {
	  
	  private Socket s;
	  private Scanner in;
	  private PrintWriter out;	  
	  //private Boolean isProviderSignIn =false;		  	
	  
	  //ArrayDeque<String> emergencyQueue = new ArrayDeque<String>();
	  
	  public ClientService(Socket s) {
		    this.s = s;
	  }
	  
	  public void run() {
		    try {
			try {
				  System.out.println("socket " + s.toString());
				  in = new Scanner(s.getInputStream());
				  out = new PrintWriter(s.getOutputStream(), true);
				  doService();

			} finally {
				  s.close();
			}
		    } catch (IOException e) {
				e.printStackTrace();
		    }		    
	  }
	  
	  public void doService() throws IOException {
		    
		    while (true) {
		    if (!in.hasNext()) return;	
		    
	  	    String line = in.nextLine();
		    System.out.println("line " + line);		

		    String code = getRequestCode(line);		    
		    String content = null;

		    System.out.println("request code " + code);
		    
		    if (code.equals("patientForgetPassword") || code.equals("providerForgetPassword")) {
				RetrievePassword r = new RetrievePassword();
				content = r.executeSqlQuery(line);

		    } else if (code.equals("providerRegister")) {				
				ProviderRegister pr = new ProviderRegister();
				content = pr.executeSqlQuery(line);

		    } else if (code.equals("providerlogin")) {				
				System.out.println("s " + s.toString());				
				ProviderLogin pl = new ProviderLogin();
				content = pl.executeSqlQuery(line);
				
				// System.out.println("Provider Login status " + isProviderSignIn);
				
				if (ProviderLogin.getLoginStatus()) {
					  ProviderStatus.setStatusLogin();
				} 
				
				//System.out.println("Provider Login status " + isProviderSignIn);
		    } else if (code.equals("providerlogout")) {
				//isProviderSignIn = false;
				JSONObject object = new JSONObject();
				object.put("status", true);
				content = object.toString();
				ProviderStatus.setStatusLogoff();
		    
		    } else if (code.equals("displayTable")) {			
				
				System.out.println("in displayTable");
				DisplayTable d = new DisplayTable();
				content = d.executeSqlQuery(line);

		    } else if (code.equals("subscribePatient")) {				
				SubscribePatient sp = new SubscribePatient();
				content = sp.executeSqlQuery(line);

		    } else if (code.equals("patientActivateAccount")) {
				PatientActivateAccount p = new PatientActivateAccount();
				content = p.executeSqlQuery(line);

		    } else if (code.equals("patientDisplayTable")) {
				PatientDisplayTable p = new PatientDisplayTable();
				content = p.executeSqlQuery(line);

		    } else if (code.equals("patientLogin")) {
				PatientLogin pl = new PatientLogin();
				content = pl.executeSqlQuery(line);

		    } else if (code.equals("patientModifyAccount")) {
				PatientModifyAccount p = new PatientModifyAccount();
				content = p.executeSqlQuery(line);
		    } else if (code.equals("patientEnterManually")) {
				PatientEnterManually p = new PatientEnterManually();
				content = p.executeSqlQuery(line);
				
		    } else if (code.equals("requestproviderinfo")) {
				RequestProviderInfo r = new RequestProviderInfo();
				content = r.executeSqlQuery(line);
				
		    } else if (code.equals("patientemergencyrequest")) {
				
				JSONObject object = new JSONObject();
				//check if the health care provider is online (sign-in?)
				//health care provider is online/sign in
				//put the 'emergency request' to the 'queue'
				//System.out.print("Provider Status " + isProviderSignIn);
				if (ProviderStatus.getProviderStatus()) {						  					  
					  ProviderStatus.emergencyQueue.add(line);					 
					  object.put("status", true);
					  content = object.toString();
				 //health care provider is offline, return "status" = false
				//client needs to send sms or email directly to the health care provider
				} else {	   					  
					  ProviderNotOnline p = new ProviderNotOnline();
					  content = p.executeSqlQuery(line);
				}
				
	  	    } else if (code.equals("checkAlertMessage")) {
				
//				 JSONObject obj = new JSONObject();
//				 obj.put("latitude", 37.654285);
//				 obj.put("longitude", -122.429951);
//				 obj.put("patientname", "kvk");
//				 obj.put("message", "Emergency Request");
//				 String tempp = obj.toString();
//				 
//				 emergencyQueue.add(tempp);
				
				System.out.println("obj json " + line);
				JSONObject object = new JSONObject();
				
				if (ProviderStatus.emergencyQueue.isEmpty()) {					  
					  object.put("status", false);
					  content = object.toString();
				} else {
					  String temp = ProviderStatus.emergencyQueue.pop();
					  object = (JSONObject) JSONValue.parse(temp);
					  object.put("status", true);
					  System.out.println("object " + object);
					  content = object.toString();
				}
				
		    } else if (code.equals("displayChart")) {
				DisplayChart d = new DisplayChart();
				content = d.executeSqlQuery(line);
				
		    }
	  	    System.out.println("javaserver content : " + content);
		    out.println(content);
		}		
	
	  } 

	  public static String getRequestCode(String json) {
		JSONObject obj = (JSONObject) JSONValue.parse(json);
		return obj.get("code").toString();
	}	
	  
}
