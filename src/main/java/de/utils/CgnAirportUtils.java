package de.utils;

import de.data.Airport;
import de.data.Flight;
import de.data.Quelle;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CgnAirportUtils extends Utils {

    private static final Logger log = LoggerFactory.getLogger(CgnAirportUtils.class);

    public ArrayList<Flight> getCgnAirportDelay(int delay) {
        ArrayList<Flight> flightArrayList = new ArrayList<>();

        try {

            Document doc = Jsoup.connect("https://www.koeln-bonn-airport.de/index.php?tx_cgnflightschedule_flightschedule%5Bpage%5D=0&cid=flights&id=14&type=3&L=0&no_cache=1&tx_cgnflightschedule_flightschedule%5Bcontroller%5D=FlightSchedule&tx_cgnflightschedule_flightschedule%5Baction%5D=flightSearch&tx_cgnflightschedule_flightschedule%5Bmode%5D=D&tx_cgnflightschedule_flightschedule%5BlimitFrom%5D=&tx_cgnflightschedule_flightschedule%5Bflightsperpage%5D=ALL&tx_cgnflightschedule_flightschedule%5BSTART%5D=&tx_cgnflightschedule_flightschedule%5BEND%5D=&tx_cgnflightschedule_flightschedule%5Bdestination%5D=&tx_cgnflightschedule_flightschedule%5BdtpSTARTDATE%5D=&tx_cgnflightschedule_flightschedule%5Bairline%5D=&tx_cgnflightschedule_flightschedule%5Btolerance%5D=3").get();
            Elements flights = doc.getElementsByClass("panel flight");

            //Keine Fluege gefunden
            if (flights.size() == 0) {
                log.error("Error: Keine Fluege am Airport gefunden \n" + doc);
                return null;
            }

            for (int i = 0; i < flights.size(); i++) {
                try {
                    Element flightHeader = flights.get(i).getElementsByClass("row flightheader").get(0);
                    Elements div = flightHeader.children();

                    Flight flight = new Flight();

                    flight.setQuelle(Quelle.AIRPORT);
                    flight.setDaid(Airport.CGN);
                    flight.setAaid(div.get(0).children().get(0).getElementsByTag("span").get(0).text()); //Flugziel
                    flight.setFc(div.get(0).children().get(0).getElementsByTag("nobr").get(0).text()); // Flugnummer

                    String status = div.get(1).children().get(0).children().get(0).text();
                    String zeit = div.get(1).children().get(0).children().get(1).text();

                    if (status.contains("verspätet")) {
                        flight = setVerspaetung(flight, zeit);

                        if (flight.getDelay() > delay) {
                            flightArrayList.add(addPreis(flight));
                        }
                    }
                } catch (Exception e) {
                    log.info("Hinweis {}", "Flug-Zeile nicht verarbeitet: " + e.getMessage());
                }
            }

            return flightArrayList;
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
            return null;
        }
    }

    private boolean isNextDay(String value) {

        boolean isNextDay = false;

        if (value.contains("Mo")) {
            isNextDay = true;
        } else if (value.contains("Di")) {
            isNextDay = true;
        } else if (value.contains("Mi")) {
            isNextDay = true;
        } else if (value.contains("Do")) {
            isNextDay = true;
        } else if (value.contains("Fr")) {
            isNextDay = true;
        } else if (value.contains("Sa")) {
            isNextDay = true;
        } else if (value.contains("So")) {
            isNextDay = true;
        }

        return isNextDay;
    }

    private String getTime(String delay, int stelle) {
        String[] split = delay.split(" ");
        int count = 0;

        for (String s : split) {
            if (s.contains(":")) {
                if (count == stelle) {
                    return s;
                } else {
                    count++;
                }
            }
        }

        return null;
    }

    public String getFlightStringCgnAirport(Flight flight) {
        String flightText = "\n" + flight.getFc() + " von " + flight.getDaid() + " nach " + flight.getAaid() + " " + flight.getDelay() + " Minuten (alt: " + getHhMm(flight.getPlanAbflugFlugzeit()) + " | neu: " + getHhMm(flight.getNeueAbflugFlugzeit()) + ")";
        return flightText;
    }

    private Flight setVerspaetung(Flight flight, String flightString) {
        String abflugAlt = getTime(flightString, 0);
        String abflugNeu = getTime(flightString, 1);

        DateTime planFlugzeit = new DateTime();
        DateTime neueFlugzeit = new DateTime();

        if (isNextDay(flightString)) {
            planFlugzeit = planFlugzeit.minusDays(1);
        }

        planFlugzeit = planFlugzeit.withHourOfDay(getStundenFromString(abflugAlt));
        planFlugzeit = planFlugzeit.withMinuteOfHour(getMinutesFromString(abflugAlt));

        neueFlugzeit = neueFlugzeit.withHourOfDay(getStundenFromString(abflugNeu));
        neueFlugzeit = neueFlugzeit.withMinuteOfHour(getMinutesFromString(abflugNeu));

        Minutes minutes = Minutes.minutesBetween(planFlugzeit, neueFlugzeit);

        flight.setPlanAbflugFlugzeit(planFlugzeit);
        flight.setNeueAbflugFlugzeit(neueFlugzeit);
        flight.setDelay(minutes.getMinutes());

        return flight;
    }

    public String getHhMm(DateTime time){
        if(time == null) return "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(time.toDate());
    }
}
