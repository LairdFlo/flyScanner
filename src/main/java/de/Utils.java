package de;

import com.google.gson.JsonObject;
import de.data.Flug;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;

public class Utils {

    public static Flug getFlug(JsonObject jo) {

        Flug flug = new Flug();

        flug.setDelay(jo.get("DDELAY").getAsInt());
        flug.setFc(cleanString(jo.get("FC").toString()));
        flug.setDaid(cleanString(jo.get("DAID").toString()));
        flug.setAaid(cleanString(jo.get("AAID").toString()));
        flug.setSsd(cleanString(jo.get("SDD").toString()));
        flug.setAdd(cleanString(jo.get("ADD").toString()));
        flug.setAad(cleanString(jo.get("AAD").toString()));
        flug.setSad(cleanString(jo.get("SAD").toString()));

        return flug;
    }

    public static JsonObjectBuilder getFlightObjekt(Flug flug) {
        JsonObjectBuilder flight = Json.createObjectBuilder();
        flight.add("FLUG", flug.getFc());
        flight.add("START", flug.getDaid());
        flight.add("ENDE", flug.getAaid());
        flight.add("DELAY", flug.getDelay());
        return flight;
    }

    public static String getFlightJson(ArrayList<Flug> flights){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrb = Json.createArrayBuilder();

        for (Flug flight : flights) {
            arrb.add(getFlightObjekt(flight));
        }

        builder.add("data", arrb);

        return builder.build().toString();

    }

    public static String getFlightString(ArrayList<Flug> flights){
        StringBuilder result = new StringBuilder();

        for (Flug flight : flights) {
            result.append("<br />" + flight.getFc() + " von " + flight.getDaid() + " nach " + flight.getAaid() + " " + flight.getDelay() + " Minuten (alt: " + flight.getSsd() + " | neu: " + flight.getAdd() + ")");
        }

        return result.toString();
    }

    private static String cleanString(String input) {
        return input.replace("\"", "").replace("[", "").replace("]", "");
    }

}
