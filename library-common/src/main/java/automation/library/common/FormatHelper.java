package automation.library.common;


import automation.library.common.listeners.AtomEventManager;
import automation.library.common.listeners.AtomEventTargetImpl;
import automation.library.common.listeners.CommonAtomEvents;
import lombok.extern.log4j.Log4j2;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Random;

@Log4j2
public class FormatHelper {

    private static AtomEventManager getEventManager() {
        return TestContext.getInstance().getAtomEventManager();
    }


    private FormatHelper(){
        throw new AtomException("Not expect to init ");
    }

//*****************************************************************************************************************//
//  Formatting data  TODO
//*****************************************************************************************************************//

    /***
     * Method to format string date split by "/"
     *
     * @param textDate: Date to be formatted as a String
     * @return : String[]
     * @author : Mel Rethlake (MBR)
     ***/
    public static String[] formatTextToDate(String textDate) {
        String[] dates = null;
        String date;
        try {
            // me;
            SimpleDateFormat desiredDateFormat = new SimpleDateFormat("mm/dd/yyyy");
            date = desiredDateFormat.format(desiredDateFormat.parse(textDate));
            log.debug("Text Date: " + textDate);
            log.debug("Formatted Date old way: " + date);
            if (date.contains("/00")) {
                log.debug("Warning: Date may not be formatted correctly");
                if (date.contains("/000") || date.contains("/001") || date.contains("/002")) {
                    date = date.replace("/00", "/20");
                } else {
                    date = date.replace("/00", "/19");
                }
            }
            dates = date.split("/");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (DateTimeParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        return dates;
    }

    /***
     * Method to format string date return a string
     *
     * @param textDate: Date to be formatted as a String
     * @return : String
     * @author : Mel Rethlake (MBR)
     ***/
    public static String formatTextToDateString(String textDate) {
        String date = null;
        try {
            SimpleDateFormat desiredDateFormat = new SimpleDateFormat("mm/dd/yyyy");
            date = desiredDateFormat.format(desiredDateFormat.parse(textDate));
            log.debug("Text Date: " + textDate);
            log.debug("Formatted Date old way: " + date);
            if (date.contains("/00")) {
                log.debug("Warning: Date may not be formatted correctly");
                if (date.contains("/000") || date.contains("/001") || date.contains("/002")) {
                    date = date.replace("/00", "/20");
                } else {
                    date = date.replace("/00", "/19");
                }
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /***
     * Method to format SSN to array
     *
     * @param SSN: Element Name
     * @return : String[]
     * @author : Mel Rethlake (MBR)
     ***/
    public static String[] formatSSN(String SSN) {
        String[] arraySSN = new String[3];

        arraySSN[0] = SSN.substring(0, 3);
        arraySSN[1] = SSN.substring(3, 5);
        arraySSN[2] = SSN.substring(5, 9);

        return arraySSN;
    }

    /***
     * Method to generate SSN (or TIN) if does not exist
     *
     * @return : SSN as a String
     * @author : Mel Rethlake(MBR) Modified By :
     ***/
    public static String generateSSN() {

        String newSSN;
        Random rand = new Random();
        int value1 = rand.nextInt(9) + 1;
        int value2 = rand.nextInt(9) + 1;
        int value3 = rand.nextInt(9) + 1;
        int value4 = rand.nextInt(9) + 1;
        int value5 = rand.nextInt(9) + 1;
        int value6 = rand.nextInt(9) + 1;
        int value7 = rand.nextInt(9) + 1;
        int value8 = rand.nextInt(9) + 1;
        int value9 = rand.nextInt(9) + 1;
        String num1 = Integer.toString(value1);
        String num2 = Integer.toString(value2);
        String num3 = Integer.toString(value3);
        String num4 = Integer.toString(value4);
        String num5 = Integer.toString(value5);
        String num6 = Integer.toString(value6);
        String num7 = Integer.toString(value7);
        String num8 = Integer.toString(value8);
        String num9 = Integer.toString(value9);
        newSSN = num1 + num2 + num3 + num4 + num5 + num6 + num7 + num8 + num9;
        getEventManager().notify(CommonAtomEvents.FORMAT_HELPER_SSN_GENERATED,new AtomEventTargetImpl<>(newSSN));
        //getReport().reportDoneEvent("Generate SSN", "SSN not in datasheet.  Randomly generated as: " + newSSN);

        return newSSN;
    }

    /***
     * Method to format TIN to array
     *
     * @param TIN : TIN as a String
     * @return : String []
     * @author : Mel Rethlake(MBR) Modified By :
     ***/
    public static String[] formatTIN(String TIN) {
        String[] arrayTIN = new String[3];

        arrayTIN[0] = TIN.substring(0, 2);
        arrayTIN[1] = TIN.substring(2, 9);

        return arrayTIN;
    }

    /***
     * Method to resolve apostrophes in xpath
     *
     * @param item: Element Name
     * @return :
     * @author : Sam Modified By :
     ***/
    public static String resolveAprostophes(String item) {
        if (!item.contains("'")) {
            return "'" + item + "'";
        }
        StringBuilder finalString = new StringBuilder();
        finalString.append("concat('");
        finalString.append(item.replace("'", "',\"'\",'"));
        finalString.append("')");
        return finalString.toString();
    }

    /***
     * Method to split string phone number by "-"
     *
     * @param phoneNumber: Phone number as a String
     * @return : String[]
     * @author : Mel Rethlake (MBR)
     ***/
    public static String[] formatPhoneNumber(String phoneNumber) {
        return phoneNumber.split("-");
    }

    /***
     * Method to split data by | - for use when indicating what fields to update vs
     * verify
     *
     * @param data: String
     * @return : String
     * @author : Mel Rethlake (MBR)
     ***/
    public static String formatUpdateData(String data) {
        String text = "";
        try {
            if (data != "" && data.contains("|")) {
                String textArray[] = data.split("\\|");
                if (textArray.length == 2) {
                    text = textArray[1];
                }
            }
        } catch (RuntimeException ex) {
         /*

           getReport().updateTestLog("Format Update Data",
                    "Error Formatting (" + data + ") EXCEPTION CAUGHT : " + ex.getMessage(), Status.FAIL);*/
        }
        return text;
    }

    /***
     * Method to split data by | - for use when looping through multiple role info
     * in same excel cell
     *
     * @param data: String
     * @return : String
     * @author : Mel Rethlake (MBR)
     ***/
    public static String formatRoleData(String data, int loop) {
        String text = "";
        try {
            if (data != "" && data.contains("|")) {
                String textArray[] = data.split("\\|");
                if (textArray.length < loop) {
                    text = "";
                } else {
                    text = textArray[loop];
                }
            } else {
                text = data;
            }
        } catch (RuntimeException ex) {

           /*

            getReport().updateTestLog("Format Update Data",
                    getReport().addModal("Error Formatting (" + text.trim() + ") EXCEPTION CAUGHT", ex.toString()),
                    Status.FAIL);*/
        }
        return text;
    }

    /***
     * Method to reformat numbers formatted as text to x,xxx.00
     *
     * @param text: String
     * @return : String
     * @author : Mel Rethlake(MBR)
     ***/
    public static String formatTextDoubleCommasTwoDecimal(String text) {

        String formattedText = "";
        try {
            if (!text.equals("")) {
                text = text.replaceAll(",", "");
                text = text.replaceAll("\\$", "");
                double amount = Double.parseDouble(text);
                DecimalFormat df = new DecimalFormat("#,##0.00");
                formattedText = df.format(amount);
                log.debug(formattedText);
            }
        } catch (RuntimeException ex) {
          /*


            getReport().updateTestLog("Format Data",
                    getReport().addModal("Error Formatting (" + text.trim() + ") EXCEPTION CAUGHT", ex.toString()),
                    Status.FAIL);*/
        }
        return formattedText;
    }

    /***
     * Method to reformat numbers formatted as text to xx.00
     *
     * @param text: String
     * @return : String
     * @author : Mel Rethlake(MBR)
     ***/
    public static String formatTextDoubleTwoDecimal(String text) {

        String formattedText = "";
        try {
            if (!text.equals("")) {
                double amount = Double.parseDouble(text);
                DecimalFormat df = new DecimalFormat("#.00");
                formattedText = df.format(amount);
                log.debug(formattedText);
            }
        } catch (RuntimeException ex) {
          /*


           getReport().updateTestLog("Format Data",
                    getReport().addModal("Error Formatting (" + text.trim() + ") EXCEPTION CAUGHT", ex.toString()),
                    Status.FAIL);*/
        }
        return formattedText;
    }

    /***
     * Method to reformat numbers formatted as text to 0.00
     *
     * @param text: String
     * @return : String
     * @author : Mel Rethlake(MBR)
     ***/
    public static String formatTextDoubleFourDecimal(String text) {

        String formattedText = "";
        try {
            if (!text.equals("")) {
                double amount = Double.parseDouble(text);
                DecimalFormat df = new DecimalFormat("0.0000");
                formattedText = df.format(amount);
                log.debug(formattedText);
            }
        } catch (RuntimeException ex) {
          /*

            getReport().updateTestLog("Format Data",
                    getReport().addModal("Error Formatting (" + text.trim() + ") EXCEPTION CAUGHT", ex.toString()),
                    Status.FAIL);*/
        }
        return formattedText;
    }

    /***
     * Method to reformat numbers formatted as text to 0.00
     *
     * @param text: String
     * @return : String
     * @author : Mel Rethlake(MBR)
     ***/
    public static String formatTextDoubleThreeDecimal(String text) {

        String formattedText = "";
        try {
            if (!text.equals("")) {
                double amount = Double.parseDouble(text);
                DecimalFormat df = new DecimalFormat("0.000");
                formattedText = df.format(amount);
                log.debug(formattedText);
            }
        } catch (RuntimeException ex) {
          /*

           getReport().updateTestLog("Format Data",
                    getReport().addModal("Error Formatting (" + text.trim() + ") EXCEPTION CAUGHT", ex.toString()),
                    Status.FAIL);*/
        }
        return formattedText;
    }

    /***
     * Method to reformat numbers formatted as text to xx.00
     *
     * @param text: String to be formatted
     * @return : double
     * @author : Mel Rethlake (MBR)
     ***/
    public static double formatTextToTwoDecimalDouble(String text) {
        double amount = 0;
        try {
            if (!text.equals("")) {
                text = text.replaceAll(",", "");
                text = text.replaceAll("\\$", "");
                amount = Double.parseDouble(text);
            }
        } catch (RuntimeException ex) {
           /*

            getReport().updateTestLog("Format Data",
                    getReport().addModal("Error Formatting (" + text.trim() + ") EXCEPTION CAUGHT", ex.toString()),
                    Status.FAIL);*/
        }
        return amount;
    }


}
