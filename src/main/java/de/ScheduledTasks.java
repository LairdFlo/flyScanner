package de;

import de.data.Flight;
import de.utils.CgnAirportUtils;
import de.utils.FlightTrackerUtils;
import de.utils.TestAndProgUtils;
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
    private TestAndProgUtils testAndProgUtils = new TestAndProgUtils();

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);

    private ArrayList<Flight> delayedFlightCache = new ArrayList();

    // Alle X Minuten (30 x 60 x 1000)
    @Scheduled(fixedRate = 900000)
    public void reportCurrentTime() {
        //Zuerst die Airport-Verspätungen einlesen und ggf. um allgemeine Verspätungen erweitern
        boolean delayCgnFlight = addNewFlight(flightTrackerCgnUtils.getCgnAirportDelay(DELAY_TIME));
        boolean delayFlight = addNewFlight(flightTrackerUtils.getDelayFlight(DELAY_TIME));

        if(delayFlight || delayCgnFlight){
            versendeEMail(testAndProgUtils.flightTime(delayedFlightCache));
        } else {
            LOG.info("Keine neuen Verspaetungen");
        }
    }

    /**
     * Erweiterung um einen neuen Flug
     * @return
     */
    private boolean addNewFlight(ArrayList<Flight> flight) {
        if(flight == null) return false;

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