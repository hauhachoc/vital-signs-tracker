/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver.sqlclasses;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 *
 * @author kelvin
 */
public class ProviderLogin {

	  private static Boolean loginStatus;
	  
	  public String executeSqlQuery(String json) {
		    
		    String response = null;
		    JSONObject obj = (JSONObject) JSONValue.parse(json);
		    
		    String sqlCommand = "SELECT COUNT(dremail) FROM providers WHERE "
					  + "password = '" + obj.get("password").toString()
					  + "' AND providerid = '" + obj.get("providerid").toString() + "';";

		    System.out.println(sqlCommand);

		    Connection connect = null;				
		    try {
				connect = SqlQueries.getConnection();
		    } catch (SQLException ex) {
				Logger.getLogger(SqlQueries.class.getName()).log(Level.SEVERE, null, ex);
		    }

		    Statement statement = null;
		    ResultSet rset = null;
		    JSONObject allPatientName = new JSONObject();
		    try {
				statement = connect.createStatement();
				rset = statement.executeQuery(sqlCommand);
				int count = 0;
				while (rset.next()) {
					  count = rset.getInt(1);						    
				}
								
				if (count > 0) {
					  sqlCommand = "SELECT patientname FROM users WHERE providerid = '"
						    + obj.get("providerid").toString() + "' ORDER BY patientname ASC;";
					  statement = connect.createStatement();
					  rset = statement.executeQuery(sqlCommand);
					  
					  int i = 1;
					  
					  while (rset.next()) {						    
						    allPatientName.put(Integer.toString(i), rset.getString("patientname"));
						    i++;
					  }
					  allPatientName.put("length", new Integer(i));
					  allPatientName.put("status", true);	
					  loginStatus = true;
				} else {
					  allPatientName.put("status", false);
					  loginStatus = false;
				}
		    } catch (SQLException e) {
				e.printStackTrace();
		    }
	  response = allPatientName.toString();   	  
	  return response;
	  }
	  
	  public static Boolean getLoginStatus() {
		    return loginStatus;
	  }
}
