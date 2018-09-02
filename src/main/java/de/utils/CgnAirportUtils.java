package de.utils;


import de.ScheduledTasks;
import de.data.Airport;
import de.data.Flight;
import de.data.Quelle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class CgnAirportUtils {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    public ArrayList<Flight> getCgnAirportDelay(int delay){
        ArrayList<Flight> flightArrayList = new ArrayList<>();

        try{

            Document doc = Jsoup.connect("https://www.koeln-bonn-airport.de/index.php?tx_cgnflightschedule_flightschedule%5Bpage%5D=0&cid=flights&id=14&type=3&L=0&tx_cgnflightschedule_flightschedule%5Bcontroller%5D=FlightSchedule&tx_cgnflightschedule_flightschedule%5Baction%5D=flightSearch&tx_cgnflightschedule_flightschedule%5Bmode%5D=D&tx_cgnflightschedule_flightschedule%5BlimitFrom%5D=&tx_cgnflightschedule_flightschedule%5Bflightsperpage%5D=ALL&tx_cgnflightschedule_flightschedule%5BSTART%5D=1524585600&tx_cgnflightschedule_flightschedule%5BEND%5D=&tx_cgnflightschedule_flightschedule%5Bdestination%5D=&tx_cgnflightschedule_flightschedule%5Bairline%5D=Eurowings").get();
            Elements flights = doc.getElementsByClass("panel flight");

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

                if(status.contains("verspätet")){

                    String[] split = zeit.split(" ");
                    flight.setSsd(split[0]);
                    flight.setAdd(split[1]);

                    flight.setDelay(getVerspaetung(flight.getSsd(), flight.getAdd()));

                    if(flight.getDelay() > delay ){
                        flightArrayList.add(flight);
                    }
                }
            }

            return flightArrayList;
        } catch (Exception e){
            log.error("Error {}", e.getMessage());
            return null;
        }
    }

    public String getFlightStringCgnAirport(Flight flight){
        String flightText = "\n" + flight.getFc() + " von " + flight.getDaid() + " nach " + flight.getAaid() + " " + flight.getDelay() + " Minuten (alt: " + flight.getSsd() + " | neu: " + flight.getAdd() + ")";
        return flightText;
    }

    private int getVerspaetung(String abflugAlt, String abflugNeu){

        int intAbflugAlt = convertStringToInt(abflugAlt);
        int intAbflugNeu = convertStringToInt(abflugNeu) ;

        if(intAbflugNeu > intAbflugAlt){
            return (intAbflugNeu - intAbflugAlt) * 60 / 100;
        } else { // Flieger startet am nächsten Tag
            int vorTagverspaetung = 24 - intAbflugAlt;
            return (vorTagverspaetung + intAbflugNeu) * 60 / 100;
        }
    }

    private int getMinutesFromString(String value){
        double minutes = Double.parseDouble(value.substring(3));
        long round = Math.round(minutes / 60 * 100);
        return (int) round;
    }

    private int getStundenFromString(String value){
        int stunden = Integer.parseInt(value.substring(0, 2));
        return stunden;
    }

    private int convertStringToInt(String value){
        int newValue = getStundenFromString(value) * 100 + getMinutesFromString(value);
        return newValue;
    }


}
