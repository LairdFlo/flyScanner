package de.utils;

import de.data.Airport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EurowingsUtils {

    private static final Logger log = LoggerFactory.getLogger(EurowingsUtils.class);

    public String getDelayPriceEurowings(Airport start, String ende, String flugnummer, String datum, boolean isFirefox) {
        WebDriverUtils webDriverUtils = new WebDriverUtils();
        String[] delayedFlights;
        if (isFirefox) {
            delayedFlights = webDriverUtils.getPricesForDelayedEurowings(start, ende, datum, webDriverUtils.getFireFoxDriver());
        } else {
            delayedFlights = webDriverUtils.getPricesForDelayedEurowings(start, ende, datum, webDriverUtils.getPhantomDriver());
        }

        if(delayedFlights != null){
            return getFlightPriceEurowings(delayedFlights, flugnummer);
        } else {
            return "";
        }
    }

    private String getFlightPriceEurowings(String[] liste, String flugnummer) {
        try {
            flugnummer = cleanEurowingsFlugnummer(flugnummer);

            for (String s : liste) {
                if (s.contains(flugnummer) && s.contains("€")) {
                    String[] euro = s.split("€");
                    String[] euroBe = euro[0].split(" ");
                    String treffer = euroBe[euroBe.length - 1];
                    treffer = treffer.replace("ab", "");
                    treffer = treffer.replace("\n", " ").replace("\r", " ");
                    return treffer;
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public String cleanEurowingsFlugnummer(String flugnummer){
        //Pruefung ob mehrere Flugnummern vergeben wurden und ggf. nach EWfiltern
        if(flugnummer.contains(",")){
            String[] split = flugnummer.split(",");

            for (String s : split) {
                if (s.contains("EW")) {
                    flugnummer = s;
                }
            }
        }

        //Bereinigung von führenden nullen

        flugnummer = String.valueOf(Integer.valueOf(flugnummer.split("EW")[1].trim()));

        return "EW" + flugnummer;
    }
}