/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jserver.sqlclasses.AbstractSql;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author kelvin
 */
public class ClientService implements Runnable {

    private Socket s;
    private Scanner in;
    private PrintWriter out;

    public ClientService(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            try {
                System.out.println("socket " + s.toString());
                in = new Scanner(s.getInputStream());
                out = new PrintWriter(s.getOutputStream(), true);

                try {
                    doService();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
                }

            } finally {
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doService() throws IOException, ClassNotFoundException,
            InstantiationException, IllegalAccessException {

        while (true) {
            if (!in.hasNext()) {
                return;
            }

            String line = in.nextLine();
            System.out.println("line " + line);

            String code = getRequestCode(line);
            System.out.println("request code " + code);
            String content = null;

            AbstractSql c = (AbstractSql) Class.forName("jserver.sqlclasses." + Hashmap.getValue(code)).newInstance();
            content = c.executeSqlQuery(line);

            System.out.println("javaserver content : " + content);
            out.println(content);
        }
    }

    public String getRequestCode(String json) {
        JSONObject obj = (JSONObject) JSONValue.parse(json);
        return obj.get("code").toString();
    }
}
