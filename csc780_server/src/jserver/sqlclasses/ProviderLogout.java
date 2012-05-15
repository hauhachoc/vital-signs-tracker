/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver.sqlclasses;

import jserver.ProviderStatus;
import org.json.simple.JSONObject;

/**
 *
 * @author kelvin
 */
public class ProviderLogout extends AbstractSql {

    private String content = null;

    @Override
    public String executeSqlQuery(String json) {
        JSONObject object = new JSONObject();
        object.put("status", true);
        content = object.toString();
        ProviderStatus.setStatusLogoff();
        CheckAlertMessage.stopCheckAlertMessageThread();
        return content;
    }
}
