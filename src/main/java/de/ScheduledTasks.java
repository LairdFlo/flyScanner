package de;

import de.data.Flug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static de.FlightChecker.*;
import static de.FlightChecker.versendeEMail;
import static de.Konfig.DELAY_TIME;
import static de.Utils.*;

@Component
public class ScheduledTasks {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);

    // Alle 15 Minuten
    @Scheduled(fixedRate = 900000)
    public void reportFlightDelays() throws Exception {
        ArrayList<Flug> delayFlight = getDelayFlight(DELAY_TIME);
        if(delayFlight.size() > 1){
            versendeEMail(getFlightString(delayFlight));
            LOG.info("Email erfolgreich verschickt");
        } else  {
            LOG.info("Keine Flugverspätung");
        }
    }
}