/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver.sqlclasses;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kelvin
 */
public class PatientLogin extends AbstractSql {

    @Override
    public String executeSqlQuery(String json) {

        System.out.println("obj json " + json);

        String sqlCommand;
        JSONObject obj = (JSONObject) JSONValue.parse(json);

        sqlCommand = "SELECT patientname  FROM users WHERE "
                + "password = '" + obj.get("password").toString()
                + "' AND email = '" + obj.get("email").toString() + "';";

        System.out.println(sqlCommand);

        Connection connect = null;
        try {
            connect = SqlQueries.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(SqlQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        JSONObject object = new JSONObject();
        Statement statement = null;
        ResultSet rset = null;
        String patientname = null;
        try {
            statement = connect.createStatement();
            rset = statement.executeQuery(sqlCommand);


            while (rset.next()) {
                patientname = rset.getString(1);
                System.out.println(patientname);
            }
            if (patientname != null) {
                object.put("status", true);
            } else {
                object.put("status", false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        object.put("patientname", patientname);
        System.out.println(object);
        return object.toString();
    }
}
