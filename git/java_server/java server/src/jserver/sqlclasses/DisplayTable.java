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
public class DisplayTable {
	  
	  public DisplayTable() {
		    
	  }
	  
	  public String executeSqlQuery(String json) {
	  	  
		    System.out.println("obj json " + json);

		    String sqlCommand = null;	  
		    JSONObject obj = (JSONObject) JSONValue.parse(json);

		    Connection connect = null;
		    try {
				connect = SqlQueries.getConnection();
		    } catch (SQLException e) {
		    }

		    Statement statement = null;
		    ResultSet rset = null;	  
		    sqlCommand = "SELECT * FROM userinfos WHERE patientname = '" 				
					  + obj.get("patientName").toString() + "'"
					  + " AND reportdate >= now()::date -  " + obj.get("period") + ";";

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

					object.put(rowNumber, row);				
					last = rset.getRow();

					System.out.println(row);
				}								
		    } catch (SQLException e) {
				e.printStackTrace();
		    }

		    object.put("numberRows", last);	  
		    return object.toString();		    		    	  
	  }
}
