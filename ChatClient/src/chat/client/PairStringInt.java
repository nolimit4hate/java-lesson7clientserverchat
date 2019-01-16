package chat.client;

import java.util.logging.Logger;

/**
 * some pair class with string and integer
 */
public class PairStringInt {

    private final static Logger LOG = Logger.getLogger(PairStringInt.class.getName());
    private String ipToConnect;
    private int portToConnect;

    public PairStringInt(String ipToConnect, int portToConnect) {
        this.ipToConnect = ipToConnect;
        this.portToConnect = portToConnect;
        LOG.fine(this.toString());
    }

    public String getIpToConnect() {
        return ipToConnect;
    }

    public int getPortToConnect() {
        return portToConnect;
    }

    @Override
    public String toString() {
        return "PairStringInt{" +
                "ipToConnect='" + ipToConnect + '\'' +
                ", portToConnect=" + portToConnect +
                '}';
    }
}
