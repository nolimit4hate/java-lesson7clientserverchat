package chat.client;

import java.io.IOException;
import java.net.SocketException;
import java.util.UUID;

import network.*;
import static network.Const.MAGIC_NUMBER;
import static network.WhatToPrint.*;

/**
 * client class. must be executed with ip:port of connection with server
 * check entering string with ip, port. create connection to the server
 * send message to sever in string format which user input in console
 */

public class Client implements TCPConnectionListener {

    /**
     * @param connection current TCP connection
     * @param magicNUmber some number that will send to server to check this client application
     * @param clientID id of client. by this id client will be registrate in server database of clients
     */

    private final int magicNumber = MAGIC_NUMBER;
    private final String clientID = UUID.randomUUID().toString();

    private TCPConnection connection;

    private Client(String ipAdress, int portNumber) {
        try {
            connection = new TCPConnection(this, ipAdress, portNumber);
        } catch (IOException ex) {
            ClientPrint.printExceptionMessage(IO_EXCEPTION, ex);
            System.exit(0);
        }
        ClientEnteringData.scannerRun(connection, clientID);
    }

    /*
     ******************************************************
     * ENTERING POINT
     ******************************************************
     */
    /**
     * check string for: 1) is there one string entering 2) is there correct format of ip and port.
     * then trying to create connection to the server
     */
    public static void main(String[] args) throws RuntimeException, SocketException {
        ClientCheckWriteSocketParams cheker = new ClientCheckWriteSocketParams();
        cheker.checkEnteringDataInMainArgs(args);
        PairStringInt ipPort = cheker.parseAddress(args[0]);
        new Client(ipPort.getIpToConnect(), ipPort.getPortToConnect());
    }

    /**
     * on connection at once send request for adding client to database of clients.
     * request in string format with MAGIC_NUMBER and client id.
     * also output information that client was connected to server
     *
     * @param tcpConnection
     */

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        connection.sendString(Integer.toString(magicNumber) + clientID);
        String hello = connection.getString();
        if(hello == null || !hello.equals(Integer.toString(MAGIC_NUMBER + 1010))){
            ClientPrint.printErrorMessage(NOT_RESPOND_REQUEST);
            System.exit(0);
        }
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String strValue) {
        System.out.println(strValue);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        ClientPrint.printErrorMessage(CLIENT_DISCONNECTED);
        tcpConnection.disconnect();
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception ex) {
        ClientPrint.printExceptionMessage(CONNECTION_EX, ex);
    }
}
