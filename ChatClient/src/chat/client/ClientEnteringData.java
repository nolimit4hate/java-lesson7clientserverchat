package chat.client;

import network.TCPConnection;

import java.util.Scanner;

public class ClientEnteringData {

    /**
     * realized console entering data by client
     */

    public static void scannerRun(TCPConnection connection, String clientID) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //adding to our string client id
                connection.sendString(line + clientID);
                // if qqb was enter then exit application
                if (line.equals("qqb")) {
                    connection.disconnect();
                    System.exit(0);
                }
            }
        }
    }
}
