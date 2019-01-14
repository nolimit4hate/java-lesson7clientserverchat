package chat.server;

import network.TCPConnection;

import java.util.List;
import java.util.Map;

import static network.WhatToPrint.MESSAGE;
import static network.WhatToPrint.SERVERONLY;

public class ServerCheckInputClientData {

    private Map<String, TCPConnection> clients;
    private List<TCPConnection> connections;
    private TCPConnection tcpConnection;

    public ServerCheckInputClientData(List<TCPConnection> connections, Map<String, TCPConnection> clients,
                                      TCPConnection tcpConnection){
        this.clients = clients;
        this.connections = connections;
        this.tcpConnection = tcpConnection;
    }

    public void checkInputData(String strValue){
        ServerOutputData serverOutput = new ServerOutputData(connections, clients);
        // id length is 36
        if (strValue != null && strValue.length() > 36) {
            String message = strValue.substring(0, strValue.length() - 36);
            String clientID = strValue.substring(strValue.length() - 36);

            // if message is qqb then print it only on server screen
            if (message.equals("qqb")) {
                serverOutput.sendToAllConnections(SERVERONLY, tcpConnection.getAdr() + ">>" + message);
                return;
            }
            // send message to all clients
            for (Map.Entry<String, TCPConnection> entry : clients.entrySet())
                if (entry.getKey().equals(clientID)) {
                    serverOutput.sendToAllConnections(MESSAGE, tcpConnection.getAdr() + ">>" + message);
                    return;
                }
        } else
            // output string messages from not client connections(another one connections) on screen
            if (strValue != null)
                serverOutput.sendToAllConnections(SERVERONLY, tcpConnection.getAdr() + ">>" + strValue);
    }

}
