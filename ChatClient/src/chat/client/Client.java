package chat.client;

import network.TCPConnection;
import network.TCPConnectionListener;

import java.io.IOException;
import java.util.Scanner;

import static chat.client.Const.*;

public class Client implements TCPConnectionListener {

    public static void main(String[] args) {
        new Client();
    }

    private TCPConnection connection;

    private Client(){
        try{
            connection = new TCPConnection(this, IP_ADDR, SOCKET_PORT);
        } catch (IOException ex){
            printMessage("Connection exception:" + ex);
        }
        Scanner scanner = new Scanner(System.in);
        while(true){
            if(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                connection.sendString(line);
            }
        }
    }



    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String strValue) {
        printMessage(strValue);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessage("Connection close...");
        connection.disconnect();
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception ex) {
        printMessage("Connection exception:" + ex);
    }

    private synchronized void printMessage(String msg){
        System.out.println(msg);
    }
}
