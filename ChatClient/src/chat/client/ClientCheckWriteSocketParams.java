package chat.client;

import AdressValidation.IPAdressValidation;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import static AdressValidation.WhatPatternUse.*;
import static network.WhatToPrint.ENTRYERROR;

public class ClientCheckWriteSocketParams {


    /**
     * check entering array of string for needed format
     */

    public void checkEnteringDataInMainArgs(String[] stringArr) throws RuntimeException, SocketException {
        IPAdressValidation validator = new IPAdressValidation(USE_PATTERN_IP_AND_PORT);
        boolean validResult = validator.validate(stringArr[0]);
        if (stringArr.length != 1 || validResult == false ||
                isIPBeginsOfMyIP(stringArr[0]) == false) {
            ClientPrint.printErrorMessage(ENTRYERROR);
            throw new RuntimeException();
        }
    }

    private boolean isIPBeginsOfMyIP(String ipString) throws SocketException{
        for(String s : getBeginsOfIP())
            if(ipString.startsWith(s))
                return true;

        return false;
    }

    private static List<String> getBeginsOfIP() throws SocketException {
        List<String> beginOfIPs = new LinkedList<>();
        IPAdressValidation validator = new IPAdressValidation(USE_PATTERN_IP);
        Enumeration enInterfaces = NetworkInterface.getNetworkInterfaces();
        while(enInterfaces.hasMoreElements()){
            NetworkInterface networkInterface = (NetworkInterface) enInterfaces.nextElement();
            Enumeration enNetworkInterfaces = networkInterface.getInetAddresses();
            while(enNetworkInterfaces.hasMoreElements()){
                InetAddress i = (InetAddress) enNetworkInterfaces.nextElement();
                String neededIPBegins = i.getCanonicalHostName();
                if(validator.validate(neededIPBegins) == true){
                    int indexOfSecondPointInString = neededIPBegins.indexOf(".", neededIPBegins.indexOf(".") + 1);
                    beginOfIPs.add(neededIPBegins.substring(0, indexOfSecondPointInString));
                }
            }
        }
        return beginOfIPs;
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
