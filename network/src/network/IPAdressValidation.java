package chat.client;

import java.util.regex.Pattern;

public class IPAdressValidation {

    /**
     * @param IP_ADRESS_PATTERN is mean that string must contain ip:port
     * example: 127.127.127.127:8088
     */


    private  static final String IP_ADRESS =
            "(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
            "(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
            "(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
            "(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])";

    private static final String PORT =
                    ":(1[1-9]\\d\\d|[2-9]\\d\\d\\d|[1-5]\\d\\d\\d\\d|6[0-4]\\d\\d\\d|65[0-4]\\d\\d)";

    private static final String IP_ADDRESS_PATTERN = "^" + IP_ADRESS + "$";
    private static final String IP_AND_PORT_PATTERN = "^" + IP_ADRESS + PORT + "$";

    private final Pattern pattern;

    public IPAdressValidation() {
        pattern = Pattern.compile(IP_ADDRESS_PATTERN);
    }

    public boolean validate(String ipAddress) {
        return pattern.matcher(ipAddress).matches();
    }

}
