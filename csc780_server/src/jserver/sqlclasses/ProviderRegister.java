/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver.sqlclasses;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author kelvin
 */
public class ProviderRegister extends AbstractSql {

    @Override
    public String executeSqlQuery(String json) {

        System.out.println("obj json " + json);

        String sqlCommand;
        JSONObject obj = (JSONObject) JSONValue.parse(json);
        //check if the email has been used, so can't use to register again.
        sqlCommand = "SELECT COUNT(drfName) FROM providers WHERE providerid = '"
                + obj.get("providerId").toString() + "';";

        Connection connect = null;
        try {
            connect = SqlQueries.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(SqlQueries.class.getName()).log(Level.SEVERE, null, ex);
        }

        JSONObject object = new JSONObject();
        Statement statement = null;
        ResultSet rset = null;
        try {
            statement = connect.createStatement();
            rset = statement.executeQuery(sqlCommand);
            int count = 0;
            while (rset.next()) {
                count = rset.getInt(1);
                System.out.println("Count " + count);
            }
            if (count > 0) {
                object.put("status", false);
                return object.toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //OK to register the account
        sqlCommand = "INSERT INTO providers "
                + "(providerid, drfname, drlname, dremail, drphone, password) VALUES('"
                + obj.get("providerId").toString() + "','"
                + obj.get("fName").toString() + "','"
                + obj.get("lName").toString() + "','"
                + obj.get("email").toString() + "','"
                + obj.get("phoneNumber").toString() + "','"
                + obj.get("password").toString() + "');";

        try {
            statement = connect.createStatement();
            rset = statement.executeQuery(sqlCommand);

        } catch (SQLException e) {
        }
        object.put("status", true);
        return object.toString();
    }
}
