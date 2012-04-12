/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver.sqlclasses;

import jserver.sqlclasses.SqlQueries;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author kelvin
 */
public class SubscribePatient {
	  
	  public String executeSqlQuery(String json) {
		    		    
		    System.out.println("obj json " + json);
		    
		    String sqlCommand;
		    JSONObject obj = (JSONObject) JSONValue.parse(json);
		
		    Connection connect = null;				
		    try {
				connect = SqlQueries.getConnection();
		    } catch (SQLException ex) {
				Logger.getLogger(SqlQueries.class.getName()).log(Level.SEVERE, null, ex);
		    }		    		    
		    
		    //OK to register the account
		    sqlCommand = "INSERT INTO users "
				+ "(providerid, patientname, email, phone, password, activate) VALUES('"
				+ obj.get("providerid").toString() + "','"
				+ obj.get("patientName").toString() + "','"					  
				+ obj.get("email").toString() + "','"
				+ obj.get("phone").toString() + "','0000', 'not')";					  

		    Statement statement = null;
		    //ResultSet rset = null;
		    try {
				statement = connect.createStatement();
				statement.executeUpdate(sqlCommand);

		    } catch (SQLException e) {
		    }
		    JSONObject object = new JSONObject();
		    object.put("status", true);
		    return object.toString();		    		    
	  }	  
}
