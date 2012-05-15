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
public class PatientModifyAccount extends AbstractSql {

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

        if ((obj.get("subcode").toString()).equals("email")) {
            sqlCommand = "UPDATE users SET email = '"
                    + obj.get("newemail").toString() + "' WHERE email = '"
                    + obj.get("oldemail").toString() + "';";

        } else if (obj.get("subcode").toString().equals("phone")) {
            sqlCommand = "UPDATE users SET phone = '"
                    + obj.get("newphone").toString() + "' WHERE email = '"
                    + obj.get("oldemail").toString() + "';";

        } else if (obj.get("subcode").toString().equals("password")) {
            sqlCommand = "UPDATE users SET password = '"
                    + obj.get("newpassword").toString() + "' WHERE email = '"
                    + obj.get("oldemail").toString() + "';";
        }

        try {
            System.out.println("sql : " + sqlCommand);
            statement = connect.createStatement();
            rset = statement.executeQuery(sqlCommand);

        } catch (SQLException e) {
        }

        JSONObject object = new JSONObject();
        object.put("status", true);
        return object.toString();
    }
}
