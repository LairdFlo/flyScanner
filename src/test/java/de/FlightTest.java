package de;

import de.data.Flug;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static de.FlightChecker.getDelayFlight;
import static de.FlightChecker.versendeEMail;
import static de.Konfig.DELAY_TIME;
import static de.Utils.getFlightString;

public class FlightTest {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Test
    public void datumTest() throws Exception {
        long time = 1524213900;

        Date date = new Date();
        date.setTime(time * 1000);

        System.out.println(dateFormat.format(new Date()));
        System.out.println(dateFormat.format(date));

    }

    @Test
    public void verschickeMail() throws Exception {
        ArrayList<Flug> flights = new ArrayList<>();
        flights.add(getBasicFlug());
        flights.add(getBasicFlug());
        flights.add(getBasicFlug());
        String flightString = getFlightString(flights);
        versendeEMail(flightString);
    }

    @Test
    public void test() throws Exception {
        String flightString = getFlightString(getDelayFlight(DELAY_TIME));
        versendeEMail(flightString);    }

    private Flug getBasicFlug(){
        Flug flug = new Flug();
        flug.setDelay(250);
        flug.setSad("");
        flug.setAad("");
        flug.setAdd("");
        flug.setAaid("");
        flug.setDaid("");
        flug.setFc("");
        flug.setSsd("");
        flug.setUnicNumber("");
        return flug;
    }
}
