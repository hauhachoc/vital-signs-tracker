package jserver;

import jserver.sqlclasses.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JavaServer {

	private final static int PORT_NUM = 8003;
}

//	public static void main(String[] args) {
//		ServerSocket serverSocket = null;
//		Socket socket = null;
//		
//		
//		while (true) {
//			try {
//				serverSocket = new ServerSocket(PORT_NUM);
//				System.out.println("Listening :8003");
//				socket = serverSocket.accept();
//				System.out.println("ip: " + socket.getInetAddress());
//				InputStream inpS = socket.getInputStream();
//				OutputStream outS = socket.getOutputStream();
//				Scanner in = new Scanner(inpS);
//				PrintWriter out = new PrintWriter(outS, true);
//			
//				while (in.hasNextLine()) {
//					  
//					String line = in.nextLine();
//					System.out.println("line " + line);
//					
//					String code = getRequestCode(line);					
//					boolean query = false;
//					String content = null;
//					
//					System.out.println("request code " + code);
//					
//					// request login
//					 if (code.equals("patientForgetPassword") || code.equals("providerForgetPassword")) {
//						RetrievePassword r = new RetrievePassword();
//						content = r.executeSqlQuery(line);						
//						
//					} else if (code.equals("providerRegister")) {
//						//query = s.insertQuery("providerRegister");
//						ProviderRegister pr = new ProviderRegister();
//						content = pr.executeSqlQuery(line);
//						  
//					} else if (code.equals("providerlogin")) {
//						  //query = s.insertQuery("providerlogin");
//						  ProviderLogin pl = new ProviderLogin();
//						  content = pl.executeSqlQuery(line);
//						  
//					} else if (code.equals("displayTable")) {
//						  DisplayTable d = new DisplayTable();
//						  content = d.executeSqlQuery(line);
//						  
//					} else if (code.equals("subscribePatient")) {
//						  //query = s.insertQuery("subscribePatient");
//						  SubscribePatient sp = new SubscribePatient();
//						  content = sp.executeSqlQuery(line);
//						  
//					} else if (code.equals("patientActivateAccount")) {
//						  PatientActivateAccount p = new PatientActivateAccount();
//						 content = p.executeSqlQuery(line);
//						 
//					} else if (code.equals("patientDisplayTable")) {
//						  PatientDisplayTable p = new PatientDisplayTable();
//						  content = p.executeSqlQuery(line);
//						  
//					} else if (code.equals("patientLogin")) {
//						  PatientLogin pl = new PatientLogin();
//						  content = pl.executeSqlQuery(line);
//						  
//					} else if (code.equals("patientModifyAccount")) {
//						  PatientModifyAccount p = new PatientModifyAccount();
//						  p.executeSqlQuery(line);
//					} else if (code.equals("patientEnterManually")) {
//						  PatientEnterManually p = new PatientEnterManually();
//						  p.executeSqlQuery(line);
//					}					  
//						
//					 System.out.println("javaserver content : " + content);
//					out.println(content);
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//			}
//
//			try {
//				socket.close();
//				socket = null;
//				serverSocket.close();
//				serverSocket = null;
//
//			} catch (Exception e) {
//			}
//		}
//	}

//	public static String getRequestCode(String json) {
//		JSONObject obj = (JSONObject) JSONValue.parse(json);
//		return obj.get("code").toString();
//	}	
//}
