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
public class RequestProviderInfo extends AbstractSql {

    @Override
    public String executeSqlQuery(String json) {

        System.out.println("obj json " + json);

        String sqlCommand;
        JSONObject obj = (JSONObject) JSONValue.parse(json);

        sqlCommand = "SELECT p.drfname, p.drlname, p.dremail, p.drphone  "
                + "FROM providers AS p, users AS u WHERE "
                + "u.email = '" + obj.get("patientemail").toString() + "' AND "
                + "u.providerid = p.providerid;";

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
        try {
            statement = connect.createStatement();
            rset = statement.executeQuery(sqlCommand);


            while (rset.next()) {
                object.put("drfname", rset.getString(1));
                object.put("drlname", rset.getString(2));
                object.put("dremail", rset.getString(3));
                object.put("drphone", rset.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        object.put("status", true);
        System.out.println(object);
        return object.toString();
    }
}
