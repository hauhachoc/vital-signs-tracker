/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author kelvin
 */
public class JavaServerThread {

    public static void main(String[] args) throws IOException {

        Hashmap.init();
        ProviderStatus.init();
        ServerSocket server = new ServerSocket(Constants.PORT);
        System.out.println("Server is listening on port 8003");

        while (true) {
            Socket s = server.accept();
            System.out.println("Client connected");
            ClientService service = new ClientService(s);
            Thread t = new Thread(service);
            t.start();
        }
    }
}
