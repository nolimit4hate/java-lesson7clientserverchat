package chat.client;

import network.TCPConnection;
import network.TCPConnectionListener;

import java.io.IOException;
import java.net.SocketException;
import java.util.UUID;
import java.util.logging.*;

import static network.Const.MAGIC_NUMBER;
import static network.WhatToPrint.*;

/**
 * client class. must be executed with ip:port of connection with server
 * check entering string with ip, port. create connection to the server
 * send message to sever in string format which user input in console
 */

public class ClientMain implements TCPConnectionListener {

    /**
     * @param connection current TCP connection
     * @param magicNUmber some number that will send to server to check this client application
     * @param clientID id of client. by this id client will be registrate in server database of clients
     */

    private final static Logger LOG = Logger.getLogger(ClientMain.class.getName());
    private final int magicNumber = MAGIC_NUMBER;
    private final String clientID = UUID.randomUUID().toString();

    private TCPConnection connection;

    private ClientMain(String ipAdress, int portNumber) {
        try {
            connection = new TCPConnection(this, ipAdress, portNumber);
            LOG.fine("ClientMain create with IP=" + ipAdress + " portNumber=" + portNumber);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "IOException in ClientMain", ex);
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
        String inputArgs = "";
        if(args.length > 0)
            for(String s : args)
                inputArgs += s;
        // logger
        LogSetup.setupLogger(LOG);
        LOG.fine("InputArgs=" + inputArgs);

        // checking entering data
        ClientCheckWriteSocketParams cheker = new ClientCheckWriteSocketParams();
        try {
            cheker.checkEnteringDataInMainArgs(args);
        } catch (RuntimeException | SocketException ex) {
            LOG.log(Level.SEVERE, "ENTRYERROR", ex);
            ClientPrint.printErrorMessage(ENTRYERROR);
            System.exit(0);
        }
        PairStringInt ipPort = cheker.parseAddress(args[0]);
        new ClientMain(ipPort.getIpToConnect(), ipPort.getPortToConnect());
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
        String requestSend = Integer.toString(magicNumber) + clientID;
        connection.sendString(requestSend);
        LOG.fine("HANDSHAKE: output request string to server=" + requestSend);
        String respondGet = connection.getString();
        LOG.fine("HANDSHAKE: input respond string from server=" + respondGet);
        if (respondGet == null || !respondGet.equals(Integer.toString(MAGIC_NUMBER + 1010))) {
            ClientPrint.printErrorMessage(NOT_RESPOND_REQUEST);
            System.exit(0);
        }
        System.out.println("You connected...\nTo quit enter qqb");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String strValue) {
        LOG.fine("input from server string=" + strValue);
        System.out.println(strValue);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        LOG.fine("Client disconnected...");
        ClientPrint.printErrorMessage(CLIENT_DISCONNECTED);
        tcpConnection.disconnect();
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception ex) {
        ClientPrint.printExceptionMessage(CONNECTION_EX, ex);
        LOG.log(Level.SEVERE, "Connection exception=", ex);
    }
}
