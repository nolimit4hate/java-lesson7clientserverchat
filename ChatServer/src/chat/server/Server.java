package chat.server;

import network.TCPConnection;
import network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static network.Const.MAGIC_NUMBER;
import static network.WhatToPrint.*;

/**
 * server class. must be executed with local port on which clients can connect with server
 * check entering string with local port. listen this local port.
 * create database of current connection(from other applications) and clients.
 * take/send messages to all clients.
 */

public class Server implements TCPConnectionListener {

    /**
     * @param connectionz is Map of clients(all connections from our application client)
     * @param connections is List of all current connections
     */

    private final Map<String, TCPConnection> connectionz = new HashMap<>();
    private final List<TCPConnection> connections = new ArrayList<>();

    private Server(int socketPort) {
        try (ServerSocket serverSocket = new ServerSocket(socketPort)) {
            serverRun(serverSocket);
        } catch (IOException ex) {
            ServerCheckEnteringData checker = new ServerCheckEnteringData();
            checker.outputErrorMessage(PORTERROR);
            throw new RuntimeException(ex);
        }
    }

    /*
     ******************************************************
     *                 ENTERING POINT
     ******************************************************
     * check entering data, load server
     */

    public static void main(String[] args) throws RuntimeException {
        ServerCheckEnteringData checker = new ServerCheckEnteringData();
        checker.checkEnteringDataInMainArgs(args);
        new Server(Integer.parseInt(args[0]));
    }

    /*
        helps constructor Server()
        running server
     */
    private void serverRun(ServerSocket serverSocket) {
        System.out.println("Server started...");
        while (true) {
            try {
                new TCPConnection(this, serverSocket.accept());
            } catch (IOException ex) {
                System.out.println("TCPConnection exception:" + ex);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
    }


    /**
     * make handshake with connection, then
     * add connection to List of connections, client(approved after handshake) to Map of clients
     *
     * @param tcpConnection current connection
     */

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        //add to all connections
        connections.add(tcpConnection);
        ServerOutputData serverOutput = new ServerOutputData(connections, connectionz);
        try {
            //check handshake message for MAGIC_NUMBER
            String helloMessage = tcpConnection.getString();
            String mNumber = Integer.toString(MAGIC_NUMBER);
            if (helloMessage != null && helloMessage.startsWith(mNumber)) {
                // send handshake message that is MAGIC_NUMBER + 1010
                tcpConnection.sendString(Integer.toString(MAGIC_NUMBER + 1010));
                String clientID = helloMessage.substring(mNumber.length());
                connectionz.put(clientID, tcpConnection);
                serverOutput.sendToAllConnections(CLIENT_CONNECTED, tcpConnection.toString());
            }
            // if didnt pass request then just keep it and output information about connection only on server screen
            else
                serverOutput.sendToAllConnections(SERVERONLY, tcpConnection.toString());
        } catch (Exception ex) {
            serverOutput.sendToAllConnections(SERVERONLY, "Connection exception");
        }
    }

    /**
     * check getting message from client for needed format. message must contain unique id
     * after checking send message to all clients
     */

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String strValue) {
        ServerCheckInputClientData inputChecker = new ServerCheckInputClientData(
                connections, connectionz, tcpConnection
        );
        inputChecker.checkInputData(strValue);
    }

    /**
     * delete this connection from all connections and Map of clients
     */

    private synchronized void dellTCPConnection(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        for (Map.Entry<String, TCPConnection> entry : connectionz.entrySet()) {
            if (tcpConnection.equals(entry.getValue())) {
                connectionz.remove(entry.getKey());
                break;
            }
        }
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        dellTCPConnection(tcpConnection);
        tcpConnection.disconnect();
        ServerOutputData serverOutput = new ServerOutputData(connections, connectionz);
        serverOutput.sendToAllConnections(CLIENT_DISCONNECTED, tcpConnection.toString());
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception ex) {
        System.out.println("TCPConnection exception:" + ex);
    }
}
