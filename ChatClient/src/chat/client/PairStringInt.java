package chat.client;

public class PairStringString {

    private String ipToConnect;
    private String portToConnect;

    public PairStringString(String ipToConnect, String portToConnect) {
        this.ipToConnect = ipToConnect;
        this.portToConnect = portToConnect;
    }

    public String getIpToConnect() {
        return ipToConnect;
    }

    private void setIpToConnect(String ipToConnect) {
        this.ipToConnect = ipToConnect;
    }

    public String getPortToConnect() {
        return portToConnect;
    }

    private void setPortToConnect(String portToConnect) {
        this.portToConnect = portToConnect;
    }
}
