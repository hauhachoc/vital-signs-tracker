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
public class PatientEnterManually extends AbstractSql {

    @Override
    public String executeSqlQuery(String json) {

        System.out.println("obj json " + json);

        String response = null;
        String sqlCommand = null;
        JSONObject obj = (JSONObject) JSONValue.parse(json);

        Connection connect = null;
        try {
            connect = SqlQueries.getConnection();
        } catch (SQLException e) {
        }

        Statement statement = null;
        //ResultSet rset = null;	  
        JSONObject object = new JSONObject();

        java.util.Date javaDate = new java.util.Date();
        long javaTime = javaDate.getTime();

        java.sql.Date sqldate = new java.sql.Date(javaTime);
        java.sql.Time sqltime = new java.sql.Time(javaTime);

        sqlCommand = "INSERT INTO userinfos "
                + "(reportdate, reporttime, patientname, measuredarm, position, systole, diastole,"
                + " heartrate, bodytemp) VALUES('" + sqldate + "', '" + sqltime + "','"
                + obj.get("patientname").toString() + "','"
                + obj.get("arm").toString() + "','"
                + obj.get("position").toString() + "',"
                + obj.get("systole") + ","
                + obj.get("diastole") + ","
                + obj.get("heartRate") + ","
                + obj.get("bodyTemp") + ");";

        System.out.println(sqlCommand);
        try {
            statement = connect.createStatement();
            statement.executeUpdate(sqlCommand);
        } catch (SQLException e) {
        }

        object.put("status", true);
        System.out.println("object : " + object);
        response = object.toString();
        return response;
    }
}
