package chat.client;

import AdressValidation.IPAdressValidation;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static AdressValidation.WhatPatternUse.USE_PATTERN_IP;
import static AdressValidation.WhatPatternUse.USE_PATTERN_IP_AND_PORT;
import static network.WhatToPrint.ENTRYERROR;

public class ClientCheckWriteSocketParams {

    private final static Logger LOG = Logger.getLogger(ClientCheckWriteSocketParams.class.getName());

    /**
     * check entering array of string for needed format
     */

    public void checkEnteringDataInMainArgs(String[] stringArr) throws RuntimeException, SocketException {
        IPAdressValidation validator = new IPAdressValidation(USE_PATTERN_IP_AND_PORT);
        boolean validResult = validator.validate(stringArr[0]);
        if (stringArr.length != 1 || validResult == false ||
                isIPBeginsOfMyIP(stringArr[0]) == false) {
            ClientPrint.printErrorMessage(ENTRYERROR);
            System.exit(0);
        }
    }

    /**
     * @return List of string that contains first two numbers of ip number of all interfaces
     * separated by point. example: from 192.168.0.188 return 192.168
     * @throws SocketException
     */

    private static List<String> getBeginsOfIP() throws SocketException {
        List<String> beginOfIPs = new LinkedList<>();
        IPAdressValidation validator = new IPAdressValidation(USE_PATTERN_IP);
        Enumeration enInterfaces = NetworkInterface.getNetworkInterfaces();
        while (enInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = (NetworkInterface) enInterfaces.nextElement();
            Enumeration enNetworkInterfaces = networkInterface.getInetAddresses();
            while (enNetworkInterfaces.hasMoreElements()) {
                InetAddress ia = (InetAddress) enNetworkInterfaces.nextElement();
                String neededIPBegins = ia.getCanonicalHostName();
                if (validator.validate(neededIPBegins) == true) {
                    int indexOfSecondPointInString = neededIPBegins.indexOf(".", neededIPBegins.indexOf(".") + 1);
                    String neededSubIP = neededIPBegins.substring(0, indexOfSecondPointInString);
                    // log
                    LOG.fine("from interface=" + enNetworkInterfaces.toString() +
                    "\n\tfrom IP=" + neededIPBegins.toString() +
                    "\n\tgotten subIP=" + neededSubIP);
                    beginOfIPs.add(neededSubIP);
                }
            }
        }
        return beginOfIPs;
    }

    /**
     * @param ipString is entering ip in string format
     * @return true if input ipString begins of any string in List of begins ip adresses of all interfaces
     * @throws SocketException
     */

    private boolean isIPBeginsOfMyIP(String ipString) throws SocketException {
        LOG.fine("isIPBeginsOfMyIP method in(" + ipString + ")\n");
        for (String s : getBeginsOfIP()) {
            LOG.fine("begin of IP=" + s);
            if (ipString.startsWith(s)) {
                LOG.fine("isIPBeginsOfMyIP method result = true");
                return true;
            }
        }
        LOG.fine("isIPBeginsOfMyIP method result = false");
        return false;
    }

    /**
     * parse string. reformat entering data
     *
     * @param vString string with ip, port
     * @return pair of string(ip) and Integer(port)
     */

    public PairStringInt parseAddress(String vString) {
        int index = vString.indexOf(":");
        String ip = vString.substring(0, index);
        String port = vString.substring(index + 1);
        LOG.fine("After parse string=" + vString);
        return new PairStringInt(ip, Integer.parseInt(port));
    }

    /**
     * is entering string is integer?
     *
     * @return true if string is integer, false if not.
     */

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}
