package chat.server;

import network.TCPConnection;
import network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import static chat.server.WhatToPrint.*;
import static chat.server.Const.SOCKET_PORT;


public class Server implements TCPConnectionListener{

    public static void main(String[] args) {
        new Server();
    }

    private final List<TCPConnection> connections = new ArrayList<>();

    private Server(){
        System.out.println("Server started...");
        try(ServerSocket serverSocket = new ServerSocket(SOCKET_PORT)){
            while(true){
                try{
                    new TCPConnection(this, serverSocket.accept());
                }catch (IOException ex){
                    System.out.println("TCPConnection exception:" + ex);
                }
            }
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections(CLIENT_CONNECTED, tcpConnection.toString());
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String strValue) {
        sendToAllConnections(MESSAGE, tcpConnection.getAdr() + ": " + strValue);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception ex) {
        System.out.println("TCPConnection exception:" + ex);
    }

    private void sendToAllConnections(WhatToPrint whatToPrint, String strValue){
        String newValue = new String();
        if(whatToPrint == CLIENT_CONNECTED)
            newValue = "Client connected:" + strValue;
        if(whatToPrint == MESSAGE)
            newValue = strValue;
        System.out.println(newValue);
        for(TCPConnection c : connections)
            c.sendString(newValue);
    }
}

