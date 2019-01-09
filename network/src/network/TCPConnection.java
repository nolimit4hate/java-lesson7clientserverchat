package network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {

    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(TCPConnectionListener eventListener, String ipAddr, int port) throws IOException {
        this(eventListener, new Socket(ipAddr, port));
    }

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.socket = socket;
        this.eventListener = eventListener;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    eventListener.onConnectionReady(TCPConnection.this);
                    while(!rxThread.isInterrupted()){
                        eventListener.onReceiveString(TCPConnection.this, in.readLine());
                    }
                    String msg = in.readLine();
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        rxThread.start();
    }

    public synchronized void sendString(String strValue){
        try{

            out.write(strValue + "\r\n");
            out.flush();
        } catch (IOException ex){
            eventListener.onException(TCPConnection.this, ex);
            disconnect();
        }
    }

    public synchronized void disconnect(){
        rxThread.interrupt();
        try{
            socket.close();
        }catch (IOException ex){
            eventListener.onException(TCPConnection.this, ex);
        }
    }
    public String getAdr(){
        return socket.getLocalAddress().getHostAddress().toString();
    }

    @Override
    public String toString() {
        return "TCPConnection{" + socket.getInetAddress() + ": " + socket.getPort() + "}";
    }
}


