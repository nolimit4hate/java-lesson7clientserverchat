package chat.client;

/**
 * some pair class with string and integer
 */
public class PairStringInt {

    private String ipToConnect;
    private int portToConnect;

    public PairStringInt(String ipToConnect, int portToConnect) {
        this.ipToConnect = ipToConnect;
        this.portToConnect = portToConnect;
    }

    public String getIpToConnect() {
        return ipToConnect;
    }

    private void setIpToConnect(String ipToConnect) {
        this.ipToConnect = ipToConnect;
    }

    public int getPortToConnect() {
        return portToConnect;
    }

    private void setPortToConnect(int portToConnect) {
        this.portToConnect = portToConnect;
    }
}
