package de.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.config.Configuration;
import de.data.Airport;
import de.data.Flight;
import de.data.Quelle;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FlightTrackerUtils extends Utils {

    private final Logger log = LoggerFactory.getLogger(FlightTrackerUtils.class);

    public ArrayList<Flight> getDelayFlight(int minDelay) {

        try {

            Gson gson = new Gson();

            ArrayList<Flight> flights = new ArrayList<>();

            for (String airport : Configuration.DATA) {
                InputStream inputStream = new URL(airport).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                JsonObject json = gson.fromJson(reader, JsonObject.class);
                JsonArray data = json.get("data").getAsJsonArray();

                for (JsonElement jsonElement : data) {

                    Flight flight =  getDelyFlightFromGermany((JsonObject) jsonElement);

                    if (flight != null && flight.getDelay() > minDelay && isPositivAirline(flight.getFc())) {
                        flights.add(addPreis(flight));
                    }
                }
            }
            return flights;
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
            return null;
        }
    }

    public Flight getDelyFlightFromGermany(JsonObject jo) {

        Airport daid = Airport.fromString(cleanString(jo.get("DAID").toString()));

        if(daid != Airport.UNKNOWN){
            Flight flight = new Flight();

            flight.setDelay(jo.get("DDELAY").getAsInt());
            flight.setFc(cleanString(jo.get("FC").toString()));
            flight.setDaid(daid);
            flight.setAaid(cleanString(jo.get("AAID").toString()));
            flight.setAad(cleanString(jo.get("AAD").toString()));
            flight.setSad(cleanString(jo.get("SAD").toString()));

            DateTime planAbflugFlugzeit = new DateTime(Long.valueOf(cleanString(jo.get("SDD").toString())) * 1000);
            DateTime neueAbflugFlugzeit = new DateTime(Long.valueOf(cleanString(jo.get("ADD").toString())) * 1000);

            flight.setPlanAbflugFlugzeit(planAbflugFlugzeit);
            flight.setNeueAbflugFlugzeit(neueAbflugFlugzeit);

            flight.setQuelle(Quelle.APP);

            return flight;
        } else {
            return null;
        }
    }

    public JsonObjectBuilder getFlightObjekt(Flight flug) {
        JsonObjectBuilder flight = Json.createObjectBuilder();
        flight.add("FLUG", flug.getFc());
        flight.add("START", flug.getDaid().toString());
        flight.add("ENDE", flug.getAaid());
        flight.add("DELAY", flug.getDelay());
        return flight;
    }

    public String getFlightJson(ArrayList<Flight> flights){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrb = Json.createArrayBuilder();

        for (Flight flight : flights) {
            arrb.add(getFlightObjekt(flight));
        }

        builder.add("data", arrb);

        return builder.build().toString();
    }

    public String getFlightString(Flight flight){
        String flightText = "\n" + flight.getFc() + " von " + flight.getDaid() + " nach " + flight.getAaid() + " " + flight.getDelay() + " Minuten (alt: " + getHhMm(flight.getPlanAbflugFlugzeit()) + " | neu: " + getHhMm(flight.getNeueAbflugFlugzeit()) + ")";
        return flightText;
    }

    private String cleanString(String input) {
        return input.replace("\"", "").replace("[", "").replace("]", "");
    }

    public String getHhMm(DateTime time){
        if(time == null) return "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(time.toDate());
    }
}
