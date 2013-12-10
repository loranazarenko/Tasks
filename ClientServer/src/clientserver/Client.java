package clientserver;

import java.io.*;
import java.net.*;

public class Client extends Thread {

    public static void main(String[] arg) throws EOFException, IOException {
        BufferedReader in = null;
        PrintWriter out = null;
        Socket clientSocket = null;

        try {

            System.out.println("server connecting....");
            clientSocket = new Socket("127.0.0.1", 9999);
            System.out.println("connection established....");

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);

            String str = "Start";
            out.println(str + "\r\n");
            String input;
            while ((input = in.readLine()) != null) {
                if (in.readLine() != "") {
                    System.out.println("echo: " + input);
                }
            }
        } catch (EOFException e) {
            System.err.println("Unexpected end of stream");
        }
        out.close();
        in.close();
        clientSocket.close();
        System.exit(0);
    }
}
