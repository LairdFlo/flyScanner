package de.utils;

import de.config.Configuration;
import de.data.Airport;
import de.data.Flight;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.config.Configuration.NEGATIV_AIRLINE;

public class Utils extends EurowingsUtils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    protected Flight addPreis(final Flight flight) {
        Flight copyFlight = flight;

        //Preisberechnung bei Eurowingsfluegen
        if (copyFlight.getFc().contains("EW") && Configuration.CALC_EW_PRICE) {
            LocalDate date = LocalDate.now();
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            String delayPrice = getDelayPriceEurowings(Airport.CGN, copyFlight.getAaid(), copyFlight.getFc(), fmt.print(date), false);
            copyFlight.setPreis(delayPrice);
        }

        return copyFlight;
    }

    /**
     * Minuten aus dem Format XX:10 ermitteln
     *
     * @param value
     * @return
     */
    protected int getMinutesFromString(String value) {
        int minutes = Integer.parseInt(value.substring(3));
        return minutes;
    }

    /**
     * Stunden aus dem Format 20:XX ermitteln
     *
     * @param value
     * @return
     */
    protected int getStundenFromString(String value) {
        int stunden = Integer.parseInt(value.substring(0, 2));
        return stunden;
    }

    /**
     * Nur von diesen Airlines sollen Verspaetungen verarbeitet werden
     *
     * @return
     */
    protected boolean isPositivAirline(String fc) {

        for (String airline : NEGATIV_AIRLINE) {
            if(fc.contains(airline)){
                return false;
            }
        }
        return true;
    }
}
