package de.utils;


import de.ScheduledTasks;
import de.data.Airport;
import de.data.Flight;
import de.data.Quelle;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;



public class CgnAirportUtils {

    private static final Logger log = LoggerFactory.getLogger(CgnAirportUtils.class);

    public ArrayList<Flight> getCgnAirportDelay(int delay) {
        ArrayList<Flight> flightArrayList = new ArrayList<>();

        try {

            Document doc = Jsoup.connect("https://www.koeln-bonn-airport.de/index.php?tx_cgnflightschedule_flightschedule%5Bpage%5D=0&cid=flights&id=14&type=3&L=0&no_cache=1&tx_cgnflightschedule_flightschedule%5Bcontroller%5D=FlightSchedule&tx_cgnflightschedule_flightschedule%5Baction%5D=flightSearch&tx_cgnflightschedule_flightschedule%5Bmode%5D=D&tx_cgnflightschedule_flightschedule%5BlimitFrom%5D=&tx_cgnflightschedule_flightschedule%5Bflightsperpage%5D=ALL&tx_cgnflightschedule_flightschedule%5BSTART%5D=&tx_cgnflightschedule_flightschedule%5BEND%5D=&tx_cgnflightschedule_flightschedule%5Bdestination%5D=&tx_cgnflightschedule_flightschedule%5BdtpSTARTDATE%5D=&tx_cgnflightschedule_flightschedule%5Bairline%5D=&tx_cgnflightschedule_flightschedule%5Btolerance%5D=3").get();
            Elements flights = doc.getElementsByClass("panel flight");

            if(flights.size() == 0) return null;

            for (int i = 0; i < flights.size(); i++) {
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

                    flight.setSsd(getTime(zeit, 0));
                    flight.setAdd(getTime(zeit, 1));

                    flight.setDelay(getVerspaetung(flight.getSsd(), flight.getAdd(), isNextDay(status)));

                    if (flight.getDelay() > delay) {
                        //Preisberechnung bei Eurowingsfluegen
                        if(flight.getFc().contains("EW")){
                            LocalDate date = LocalDate.now();
                            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                            CgnAirportUtils cgnAirportUtils = new CgnAirportUtils();
                            String delayPrice = cgnAirportUtils.getDelayPriceEurowings(Airport.CGN, flight.getAaid(), flight.getFc(), fmt.print(date), false);
                            flight.setPreis(delayPrice);
                        }

                        flightArrayList.add(flight);
                    }
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
        String flightText = "\n" + flight.getFc() + " von " + flight.getDaid() + " nach " + flight.getAaid() + " " + flight.getDelay() + " Minuten (alt: " + flight.getSsd() + " | neu: " + flight.getAdd() + ")";
        return flightText;
    }

    private int getVerspaetung(String abflugAlt, String abflugNeu, boolean isNextDayFlight) {
        DateTime planFlugzeit = new DateTime();
        DateTime neueFlugzeit = new DateTime();

        if (isNextDayFlight) {
            planFlugzeit = planFlugzeit.minusDays(1);
        }

        planFlugzeit = planFlugzeit.withHourOfDay(getStundenFromString(abflugAlt));
        planFlugzeit = planFlugzeit.withMinuteOfHour(getMinutesFromString(abflugAlt));

        neueFlugzeit = neueFlugzeit.withHourOfDay(getStundenFromString(abflugNeu));
        neueFlugzeit = neueFlugzeit.withMinuteOfHour(getMinutesFromString(abflugNeu));

        Minutes minutes = Minutes.minutesBetween(planFlugzeit, neueFlugzeit);
        return minutes.getMinutes();

    }

    private int getMinutesFromString(String value) {
        int minutes = Integer.parseInt(value.substring(3));
        return minutes;
    }

    private int getStundenFromString(String value) {
        int stunden = Integer.parseInt(value.substring(0, 2));
        return stunden;
    }

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
