/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver.sqlclasses;

import jserver.ProviderStatus;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author kelvin
 */
public class CheckAlertMessage extends AbstractSql {

    private static String content;
    private static boolean checkQueueStack;

    @Override
    public String executeSqlQuery(String json) {

        System.out.println("obj json " + json);
        JSONObject object;
        checkQueueStack = true;

        System.out.println("thread is listening for incoming alert message ... ");

        while (checkQueueStack) {
            if (!ProviderStatus.emergencyQueue.isEmpty()) {
                System.out.println("pop queue");
                String temp = ProviderStatus.emergencyQueue.pop();
                object = (JSONObject) JSONValue.parse(temp);
                object.put("status", true);
                object.put("code", "patientemergencyrequest");
                System.out.println("object " + object);
                content = object.toString();
                checkQueueStack = false;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public static void stopCheckAlertMessageThread() {
        checkQueueStack = false;
        JSONObject object = new JSONObject();
        object.put("status", false);
        content = object.toString();
    }
}
