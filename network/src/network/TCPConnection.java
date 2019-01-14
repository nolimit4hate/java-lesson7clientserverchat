package network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * This our connection class. Obj of this class is one connection uses TCP protocol realized with client server
 * architecture.
 */

public class TCPConnection {

    /**
     * @param socket is socket of connection(connection of ports)
     * @param rxTread is Thread in whitch this connection will be process
     * @param eventListener is connection listener. Listen connection status, input string stream.
     * @param in is input stream
     * @param out is output stream
     */

    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    /**
     * this constructor for client connection to server
     *
     * @param ipAddr ip of needed server in string format
     * @param port   local port of needed server
     * @throws IOException
     */

    public TCPConnection(TCPConnectionListener eventListener, String ipAddr, int port) throws IOException {
        this(eventListener, new Socket(ipAddr, port));
    }

    /**
     * this construcotr for server connection to client
     *
     * @param socket is socket of current connection
     * @throws IOException
     */

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.socket = socket;
        this.eventListener = eventListener;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        // create new thread for eventListener
        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this);

                    while (!rxThread.isInterrupted()) {
                        String inputMessage = in.readLine();
                        // disconnect current connection if server/client was disconnected
                        if (inputMessage == null)
                            disconnect();
                        eventListener.onReceiveString(TCPConnection.this, inputMessage);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        rxThread.start();
    }




    public synchronized void sendString(String strValue) {
        try {
            out.write(strValue + "\r\n");
            out.flush();
        } catch (IOException ex) {
            eventListener.onException(TCPConnection.this, ex);
            disconnect();
        }
    }

    public synchronized String getString() {
        try {
            return in.readLine();
        } catch (IOException ex) {
            eventListener.onException(TCPConnection.this, ex);
            disconnect();
        }
        return null;
    }

    /**
     * interrupt thread in which connection processed and close socket of connection
     */

    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException ex) {
            eventListener.onException(TCPConnection.this, ex);
        }
    }

    /**
     * @return ip and current port of this connection in string format
     */

    public String getAdr() {
        return socket.getLocalAddress().getHostAddress().toString() +
                ":" + socket.getPort();
    }

    @Override
    public String toString() {

        return "TCPConnection{" + socket.getInetAddress() + ":" + socket.getPort() + "}";
    }
}


