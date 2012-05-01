/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver.sqlclasses;

import java.sql.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author kelvin
 */
public class PatientDisplayTable {

	  public String executeSqlQuery(String json) {
		    
		    String response = null;
		    
		    JSONObject obj = (JSONObject) JSONValue.parse(json);

		    System.out.println("json in getTableInfo " + obj.toString());
		    
		    Connection connect = null;
		    try {
				connect = SqlQueries.getConnection();
		    } catch (SQLException e) {
		    }
		    
		    Statement statement = null;
		    ResultSet rset = null;
		    String sqlCommand = "SELECT * FROM userinfos as u, users as s WHERE "
				+ "s.email = '" + obj.get("patientEmail").toString() + "' AND "
				+ "s.patientname = u.patientname;"; 									    		    		    		    
		    System.out.println("sql : " + sqlCommand);
		    
		    JSONObject object = new JSONObject();		    		    
		    int last = 0;
		    
		    try {
				statement = connect.createStatement();
				rset = statement.executeQuery(sqlCommand);
				
				while (rset.next()) {
					String rowNumber = Integer.toString(rset.getRow());  					 					
															
					String row = rset.getDate("reportdate") + "\t\t" 
						     + rset.getInt("systole") + "/"
						     + rset.getInt("diastole") + "\t\t\t\t\t\t\t\t"
						     + rset.getInt("heartrate") + "\t\t\t\t\t\t\t"
						     + rset.getInt("bodytemp");
					
					System.out.println("row " + rset.getRow() + " : " + row);
					
					object.put(rowNumber, row);				
					last = rset.getRow();
				}								
		    } catch (SQLException e) {
				e.printStackTrace();
		    }
		    
		    object.put("numberRows", last);
		    response = object.toString();		    		    
		    
		    return response;
	  }
}
