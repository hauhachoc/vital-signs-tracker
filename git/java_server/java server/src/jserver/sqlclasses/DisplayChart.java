/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver.sqlclasses;

import java.sql.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
/**
 *
 * @author kelvin
 */
public class DisplayChart {
	  
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
	  sqlCommand = "SELECT to_char(reportdate, 'FMDD') as date, systole, diastole "
		    + "FROM userinfos WHERE patientname = '" 				
		    + obj.get("patientName").toString() + "'"
		    + " AND reportdate >= now()::date -  " + obj.get("period") + ";";
		    		    		    		    
	  JSONObject object = new JSONObject();
	  JSONArray listSystole = new JSONArray();
	  JSONArray listDiastole = new JSONArray();
	  JSONArray listDate = new JSONArray();
	  int last = 0;

	  try {
		    statement = connect.createStatement();
		    rset = statement.executeQuery(sqlCommand);

		    while (rset.next()) {
			   //String rowNumber = Integer.toString(rset.getRow());  					 					
			    listDate.add(rset.getString("date"));
			    listSystole.add(rset.getInt("systole"));
			    listDiastole.add(rset.getInt("diastole"));			    
			    last = rset.getRow();
			    
			    //System.out.println(row);
		    }								
	  } catch (SQLException e) {
		    e.printStackTrace();
	  }
	  object.put("size", last);
	  object.put("listDate", listDate);	  
	  object.put("listSystole", listSystole);	
	  object.put("listDiastole", listDiastole);	
	  object.put("status", true);
	  System.out.println(object);
	  return object.toString();		    		    	  
	  }
}
