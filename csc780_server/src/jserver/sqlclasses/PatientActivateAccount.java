/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver.sqlclasses;

import java.sql.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @author kelvin
 */
public class PatientActivateAccount extends AbstractSql {

    @Override
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

        sqlCommand = "UPDATE users SET password = '"
                + obj.get("password").toString() + "', activate = 'true' WHERE email = '"
                + obj.get("email").toString() + "'";

        try {
            statement = connect.createStatement();
            rset = statement.executeQuery(sqlCommand);

        } catch (SQLException e) {
        }

        JSONObject object = new JSONObject();
        object.put("status", true);
        return object.toString();
    }
}
