/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver;

import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author kelvin
 */
public class SocketList {
	  
	  public static HashMap <String, Socket> providers = new HashMap<String, Socket>();
	  
	  public static Socket getSocket(String key) {
		    return providers.get(key);
	  }
	  
}
