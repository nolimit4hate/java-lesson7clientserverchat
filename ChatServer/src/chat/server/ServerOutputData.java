package chat.server;

import network.TCPConnection;
import network.WhatToPrint;

import java.util.List;
import java.util.Map;

import static network.WhatToPrint.*;

public class ServerOutputData {

    private Map<String, TCPConnection> clients;
    private List<TCPConnection> connections;

    public ServerOutputData(List<TCPConnection> connections, Map<String, TCPConnection> clients) {
        this.clients = clients;
        this.connections = connections;
    }

    /**
     * what print on server screen and what send to all or client connections
     * depends from whatToPrint
     *
     * @param whatToPrint is key of printing and messaging logic
     * @param strValue    some entering string
     */

    public synchronized void sendToAllConnections(WhatToPrint whatToPrint, String strValue) {
        String newValue = new String();
        if (whatToPrint == CLIENT_CONNECTED)
            newValue = "Client connected:" + strValue;
        if (whatToPrint == CLIENT_DISCONNECTED)
            newValue = "Client disconnected:" + strValue;
        if (whatToPrint == CLIENT_DISCONNECTED || whatToPrint == CLIENT_CONNECTED) {
            for (TCPConnection c : connections)
                c.sendString(newValue);
            System.out.println(newValue);
            return;
        }
        if (whatToPrint == MESSAGE) {
            newValue = strValue;
            System.out.println(newValue);
            for (Map.Entry<String, TCPConnection> entry : clients.entrySet()) {
                entry.getValue().sendString(newValue);
            }
        }
        if (whatToPrint == SERVERONLY)
            System.out.println(strValue);
    }
}
