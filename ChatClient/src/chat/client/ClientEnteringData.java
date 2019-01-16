package chat.client;

import network.TCPConnection;

import java.util.Scanner;
import java.util.logging.Logger;

public class ClientEnteringData {

    private final static Logger LOG = Logger.getLogger(ClientEnteringData.class.getName());

    /**
     * realized console entering data by client
     */

    public static void scannerRun(TCPConnection connection, String clientID) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                LOG.fine("scanner.nextline=" + line);
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
