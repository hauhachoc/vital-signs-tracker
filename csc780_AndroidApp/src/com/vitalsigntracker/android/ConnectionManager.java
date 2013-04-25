/* ConnectionManager class takes care the client-server
 * connection (socket connection).
 * It opens the socket connection between client and server.
 * 
 */
package com.vitalsigntracker.android;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

import metadata.Constants;

public class ConnectionManager {

	private static InputStream inpS;
	private static OutputStream outS;

	/*
	 * @param (String json) To be sent to the server
	 * @return (String response) It is a response (JSON
	 * 		String) from server.
	 */
	public static String connect(String json) {

		String response = null;

		try {
			SocketAddress socketAddress = new InetSocketAddress(
					Constants.IP_ADD, Constants.PORT);
			Socket sock = new Socket();
			sock.connect(socketAddress, 10 * 1000);
			inpS = sock.getInputStream();
			outS = sock.getOutputStream();
			Scanner in = new Scanner(inpS);
			PrintWriter out = new PrintWriter(outS, true);
			out.println(json);
			response = in.nextLine();
			inpS.close();
			out.close();
			outS.close();
			sock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
