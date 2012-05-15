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
public class RetrievePassword extends AbstractSql {

    @Override
    public String executeSqlQuery(String json) {

        System.out.println("obj json " + json);

        String sqlCommand = null;
        String password = null;
        JSONObject obj = (JSONObject) JSONValue.parse(json);

        Connection connect = null;
        try {
            connect = SqlQueries.getConnection();
        } catch (SQLException e) {
        }

        JSONObject object = new JSONObject();
        Statement statement = null;
        ResultSet rset = null;

        if ((obj.get("code").toString()).equals("1")) {		 //"patientForgetPassword"
            sqlCommand = "SELECT password FROM users WHERE "
                    + "patientname = '" + obj.get("pname").toString()
                    + "' AND email = '" + obj.get("email").toString() + "';";
        } else if ((obj.get("code").toString()).equals("2")) {			  //"providerForgetPassword"
            sqlCommand = "SELECT password FROM providers WHERE "
                    + "drfname = '" + obj.get("firstname").toString()
                    + "' AND drlname = '" + obj.get("lastname").toString() + "';";
        }

        try {
            statement = connect.createStatement();
            rset = statement.executeQuery(sqlCommand);

            while (rset.next()) {
                password = rset.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (password != null) {
            object.put("status", true);
            object.put("password", password);
        } else {
            object.put("status", false);
        }
        return object.toString();
    }
}
