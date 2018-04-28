package de.utils;

import de.ScheduledTasks;
import de.data.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import static de.config.Configuration.*;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private FlightTrackerUtils flightTrackerUtils = new FlightTrackerUtils();
    private CgnAirportUtils flightTrackerCgnUtils = new CgnAirportUtils();

    public String flightTime(ArrayList<Flight> flights) {
        String flightText = "";

        for (Flight flight : flights) {
            if (flight.isCgnFlight()) {
                flightText += flightTrackerCgnUtils.getFlightStringCgnAirport(flight);
            } else {
                flightText += flightTrackerUtils.getFlightString(flight);
            }
        }
        return flightText;
    }

}
