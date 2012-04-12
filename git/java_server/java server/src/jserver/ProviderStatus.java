/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver;

import java.util.ArrayDeque;

/**
 *
 * @author kelvin
 */
public class ProviderStatus {
	  
	  public static ArrayDeque<String> emergencyQueue = new ArrayDeque<String>();
	  public static Boolean isProviderLogin = false;
	  
	  public static void setStatusLogin() {
		    isProviderLogin = true;
	  }
	  
	  public static void setStatusLogoff() {
		    isProviderLogin = false;
	  }
	  
	  public static Boolean getProviderStatus() {
		    return isProviderLogin;
	  }
	  
}
