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
import jserver.ProviderStatus;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author kelvin
 */
public class PatientEmergencyRequest extends AbstractSql {

    private String content = null;

    @Override
    public String executeSqlQuery(String json) {

        JSONObject object = new JSONObject();
        //check if the health care provider is online (sign-in?)
        //health care provider is online/sign in
        //put the 'emergency request' to the 'queue'
        System.out.print("Provider Status " + ProviderStatus.getProviderStatus());
        if (ProviderStatus.getProviderStatus()) {
            ProviderStatus.emergencyQueue.add(json);
            System.out.println("emergencyQueue " + ProviderStatus.emergencyQueue.peek());
            object.put("status", "true");
            content = object.toString();

        } else {
            //health care provider is offline, return "status" = false
            //client needs to send sms or email directly to the health care provider								
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

            Statement statement = null;
            ResultSet rset = null;
            String drphone = null;
            try {
                statement = (Statement) connect.createStatement();
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
        return content;
    }
}
