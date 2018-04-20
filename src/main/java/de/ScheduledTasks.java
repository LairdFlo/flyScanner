package de;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static de.FlightChecker.versendeEMail;
import static de.Konfig.DELAY_TIME;

@Component
public class ScheduledTasks {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);

    //60000 => 1 Minute

    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() throws Exception {
        String delays = FlightChecker.flightSearch(DELAY_TIME, false);
        versendeEMail(delays);
        LOG.info("Email erfolgreich verschickt");
    }
}