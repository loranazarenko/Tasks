package clientserver;

import java.io.*;
import static java.lang.Thread.MIN_PRIORITY;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

public class Server {

    public static void main(String[] arg) throws IOException, SQLException, ClassNotFoundException {
        ServerSocket serverSocket = null;
        Socket clientAccepted = null;
        BufferedReader in = null;
        PrintWriter out = null;
        Connection connect = null;
        Statement stm = null;

        try {
            System.out.println("server starting....");
            serverSocket = new ServerSocket(9999);
            clientAccepted = serverSocket.accept();
            System.out.println("connection established....");
            in = new BufferedReader(new InputStreamReader(clientAccepted.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientAccepted.getOutputStream())), true);
            String input;
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("Start")) {
                    Locale.setDefault(Locale.ENGLISH);
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    connect = DriverManager.getConnection("" + "jdbc:oracle:thin:@localhost:1521:XE", "system", "1");
                    try {
                        stm = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        String sql = "SELECT * FROM  CUSTOMER";
                        ResultSet rec = stm.executeQuery(sql);
                        rec.last();
                        int count = 0;
                        int resulSetSize = rec.getRow();
                        rec.beforeFirst();
                        String ss = "";

                        while (rec.next()) {
                            if (count < resulSetSize) {
                                count++;
                                String CUSTOMERID = rec.getString("CUSTOMERID");
                                String NAME = rec.getString("NAME");
                                String EMAIL = rec.getString("EMAIL");
                                String COUNTRYCODE = rec.getString("COUNTRYCODE");
                                int BUDGET = rec.getInt("BUDGET");
                                int USED = rec.getInt("USED");

                                ss = CUSTOMERID + " " + NAME + " " + EMAIL + " " + COUNTRYCODE + " " + BUDGET + " " + USED;
                                System.out.println("ROW = " + ss);
                                if (! ss.equals("") && ss != null) {
                                    out.println(ss + "\r\n");
                                }
                            } else {
                                out.println("BAD" + "\r\n");
                            }
                        }
                        try {
                            connect.close();
                            in.close();
                            out.close();
                            serverSocket.close();
                            clientAccepted.close();
                        } catch (IOException e) {
                           System.out.println("Stream closed");
                            System.out.println("The END!");
                            System.exit(0);
                        }
                    } catch (SQLException er) {
                        System.out.println("SELECT is wrong!");
                        System.out.println(er.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Stream closed");
            System.out.println("The END!");
            System.exit(0);
        }
    }
}
