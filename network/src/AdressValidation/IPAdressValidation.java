package AdressValidation;

import java.util.regex.Pattern;

import static AdressValidation.WhatPatternUse.*;

/**
 * build regEx for IP and IP:local port. make validation of its.
 */
public class IPAdressValidation {

    /**
     *
     * @param IP_ADRESS - string that help to build regEx of IP adress.
     *                    4 times number 0..255 with point selector
     * @param PORT string that help to build regEx of IP adress and port.
     *             number 1100..65499
     * @param pattern pattern of one of this string(PORT, IP_ADRESS_PATTERN, IP_AND_PORT_PATTERN)
     */

    private  static final String IP_ADRESS =
            "(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
            "(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
            "(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
            "(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])";

    private static final String PORT =
                    "(1[1-9]\\d\\d|[2-9]\\d\\d\\d|[1-5]\\d\\d\\d\\d|6[0-4]\\d\\d\\d|65[0-4]\\d\\d)";

    private Pattern pattern;

    public IPAdressValidation(WhatPatternUse wpu) {
        // use IP pattern
        if(wpu == USE_PATTERN_IP)
            pattern = Pattern.compile("^" + IP_ADRESS + "$");
        // use IP:port pattern
        if(wpu == USE_PATTERN_IP_AND_PORT)
            pattern = Pattern.compile("^" + IP_ADRESS + ":" + PORT + "$");
        // use port pattern
        if(wpu == USE_PATTERN_PORT)
            pattern = Pattern.compile("^" + PORT + "$");
    }

    /**
     * @param vString IP or IP:port in string format
     * @return true if vString matches needed regEx
     */

    public boolean validate(String vString) {
        return pattern.matcher(vString).matches();
    }

}
