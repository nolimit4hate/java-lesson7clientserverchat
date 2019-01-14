package chat.client;

import network.WhatToPrint;

import static network.WhatToPrint.*;

public class ClientPrint {

    /**
     * some method for print errors information to client
     *
     * @param whatToPrint
     */

    public static void printErrorMessage(WhatToPrint whatToPrint) {
        if (whatToPrint == ENTRYERROR)
            System.err.println("Use jar like: java -jar jarName.jar IP_ADRESS:PORT_NUMBER\n" +
                    "When IP_ADRESS is four integer separated by . in range from 0..255\n" +
                    "When PORT_NUMBER is integer in range from 1100..65500\n" +
                    "Example: java -jar Client.jar 127.127.127.127:8088\n" +
                    "Also see your ip with \"ifconfig\" command");
        if (whatToPrint == NOT_RESPOND_REQUEST)
            System.err.println("Connection exception:\nServer not respond request.");
        if (whatToPrint == CLIENT_DISCONNECTED)
            System.err.println("Something goes wrong with server.\nConnection closed...");
    }

    public static void printExceptionMessage(WhatToPrint whatToPrint, Exception ex) {
        if (whatToPrint == IO_EXCEPTION)
            System.out.println("Connection exception: " + ex + "\nServer not found, try another time");
        if (whatToPrint == CONNECTION_EX)
            System.out.println(("Connection exception: " + ex + "\nConnection lost"));
        if (whatToPrint == SOCKET_EXCEPTION)
            System.out.println("Connection exception: " + ex + "\nSocket error");

    }

}
