package jserver.sqlclasses;

import java.sql.*;

public class SqlQueries {
	 
	 static String url = "jdbc:postgresql://localhost:5432/vital_signs_tracker";
	  //static String url = "jdbc:postgresql://10.0.0.3:5432/vital_signs_tracker";
	  static String db = "postgres";
	  static String pw = "sfsu";
	  
	  public static Connection getConnection() throws SQLException {		    
		    return DriverManager.getConnection(url, db, pw);				    
	  }
}
