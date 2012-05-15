/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver.sqlclasses;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author kelvin
 */
public class ProviderNotOnline extends AbstractSql {

    public String executeSqlQuery(String json) {

        System.out.println("obj json " + json);

        String sqlCommand;
        JSONObject obj = (JSONObject) JSONValue.parse(json);

        sqlCommand = "SELECT p.drphone  FROM users AS u, providers AS p WHERE "
                + "u.patientname = '" + obj.get("patientname").toString()
                + "' AND u.providerid = p.providerid;";

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
        String drphone = null;
        try {
            statement = connect.createStatement();
            rset = statement.executeQuery(sqlCommand);


            while (rset.next()) {
                drphone = rset.getString(1);
                object.put("drphone", drphone);
                System.out.println(drphone);
            }
            object.put("status", false);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(object);
        return object.toString();
    }
}
