package de;

import de.data.Flight;
import de.utils.CgnAirportUtils;
import de.utils.FlightTrackerUtils;
import de.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static de.config.Configuration.DELAY_TIME;

import static de.utils.MailUtils.versendeEMail;

@Component
public class ScheduledTasks {

    private FlightTrackerUtils flightTrackerUtils = new FlightTrackerUtils();
    private CgnAirportUtils flightTrackerCgnUtils = new CgnAirportUtils();
    private Utils utils = new Utils();

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);

    private ArrayList<Flight> delayedFlightCache = new ArrayList();

    // Alle X Minuten (30 x 60 x 1000)
    @Scheduled(fixedRate = 1800000)
    public void reportCurrentTime() {
        boolean delayFlight = addNewFlight(flightTrackerUtils.getDelayFlight(DELAY_TIME));
        boolean delayCgnFlight = addNewFlight(flightTrackerCgnUtils.getCgnAirportDelay(DELAY_TIME));

        if(delayFlight || delayCgnFlight){
            versendeEMail(utils.flightTime(delayedFlightCache));
        } else {
            LOG.info("Keine neuen Verspaetungen");
        }
    }

    /**
     * Erweiterung um einen neuen Flug
     * @return
     */
    private boolean addNewFlight(ArrayList<Flight> flight) {
        boolean newDelay = false;
        for (Flight flightNumber : flight) {
            if (delayedFlightCache.contains(flightNumber) == false) {
                LOG.info("Neue Verspaetung: " + flightNumber.getFc());
                delayedFlightCache.add(flightNumber);
                newDelay = true;
            }
        }
        return newDelay;
    }

    /**
     * Taegliches zuruecksetzen der Fluege
     */
    @Scheduled(cron="0 0 0 * * *")
    public void doScheduledWork() {
        delayedFlightCache = new ArrayList();
        LOG.info("Gespeicherte Fluege zurueckgesetzt");
    }



}