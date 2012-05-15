/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver;

import java.util.HashMap;

/**
 *
 * @author kelvin
 */
public class Hashmap {

    public static HashMap<String, String> sqlClass = new HashMap();

    public static void init() {

        sqlClass.put("1", "RetrievePassword");
        sqlClass.put("3", "PatientActivateAccount");
        sqlClass.put("5", "PatientDisplayTable");
        sqlClass.put("7", "PatientLogin");
        sqlClass.put("9", "PatientModifyAccount");
        sqlClass.put("11", "PatientEnterManually");
        sqlClass.put("13", "PatientEmergencyRequest");
        sqlClass.put("15", "RequestProviderInfo");

        sqlClass.put("2", "RetrievePassword");
        sqlClass.put("4", "ProviderRegister");
        sqlClass.put("6", "ProviderLogin");
        sqlClass.put("8", "ProviderLogout");
        sqlClass.put("10", "DisplayTable");
        sqlClass.put("12", "SubscribePatient");
        sqlClass.put("14", "DisplayChart");
        sqlClass.put("16", "CheckAlertMessage");
    }

    public static String getValue(String key) {
        return sqlClass.get(key);
    }
}