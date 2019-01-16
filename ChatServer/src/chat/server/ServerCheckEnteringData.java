package chat.server;

import AdressValidation.IPAdressValidation;
import AdressValidation.WhatPatternUse;
import network.WhatToPrint;

import static network.WhatToPrint.ENTRYERROR;
import static network.WhatToPrint.PORTERROR;

public class ServerCheckEnteringData {

    /**
     * check entering data = array of string. if data incorrect throw exception
     *
     * @param stringArr is String[] args
     */

    public static void checkEnteringDataInMainArgs(String[] stringArr) throws RuntimeException {
        IPAdressValidation validation = new IPAdressValidation(WhatPatternUse.USE_PATTERN_PORT);
        if (stringArr.length != 1 || validation.validate(stringArr[0]) == false) {
            outputErrorMessage(ENTRYERROR);
            System.exit(0);
        }
    }

    /**
     * output error information for users who execute application server with wrong entering data
     *
     * @param whatToPrint label on which output need error message
     */

    public static void outputErrorMessage(WhatToPrint whatToPrint) {
        if (whatToPrint == ENTRYERROR)
            System.err.println("Use jar like: java -jar jarName.jar PORT_NUMBER\n" +
                    "When PORT_NUMBER is integer in range from 1100 to 65500\n" +
                    "Example: java -jar ServerMain.jar 8088");
        if (whatToPrint == PORTERROR)
            System.err.println("Your port is already listened. Enter another port!");
    }

    /**
     * is entering string is integer?
     *
     * @return true if string is integer, false if not.
     */

    public static boolean isInteger(String s) {
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
