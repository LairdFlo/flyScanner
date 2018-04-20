package de;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import static de.Konfig.*;

public class FlightChecker {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    final private static String MAIN_URL = "http://flightapp.creativeapis.com/api/Services/JSON/current_departures_for_airport.php?data=";

    final private static String[] AIRPORTS = {
            MAIN_URL + "zvoOWl4vxF1gr3M6rwe475iptYMZjzdCfRw5M5iPpWWriyp%2FThgm59N1JYDunUYz%0A", // CGN = Köln
            MAIN_URL + "p8CJ%2BquXhdGjobp39tmTkMwwkBjZgfiaauvbgjpmcogTuEBXmzGnoP%2F%2FjEqF0r5C%0A", // DUS = Düsseldorf
            MAIN_URL + "Ay6raj8mqG7QZhJTxq%2BuwhSDPLocfnr4URml%2BF3gYia8pEJ43I4fj69x9ZO5TZiy%0A", // HAM = Hamburg*/
            MAIN_URL + "0TZxxiB%2BLnqIrlV%2FZQITB0XWPbrzonfsoR5dpy9q5Y92zeLa32f3%2B7QPgCjQgmM%2F%0A", // TXL = Berlin
            MAIN_URL + "W9o0P0lIPKKCjq0xdXFlMc%2BtMi3OE3l1Lgi%2BAiiavqaVAbXGVDRtdPlcJu8qHgMA%0A", // FKB = Karlsruhe / Baden-Baden
            MAIN_URL + "ValU97vvoRqjA8iXgV5EOuIJZPNu6QOnFKPyZXpoYeOrvstgYK3pIGTxi2yFj5Pk%0A", // STR = Stuttgart
            MAIN_URL + "SYI3weE%2BVQBEsMMMbU0qIu6t185f%2BQ2KCKuCIAsizxzPafO5nEglKzf8sOmbUrxj%0A", // SCN = Saarbrücken
/*            MAIN_URL + "", // X = Dortmund
            MAIN_URL + "", // X = Düsseldorfd Weeze
            MAIN_URL + "", // X = Leipzip/Halle
            MAIN_URL + "", // X = Dresden
            MAIN_URL + "", // X = Saarbrücken
            MAIN_URL + "", // X = Paderborn/Lippstadt
            MAIN_URL + "", // X = Münster/Osnabrück
            MAIN_URL + "", // X = Hannover
            MAIN_URL + "", // X = Bremen
            MAIN_URL + "", // X = Sylt
            MAIN_URL + "" // X = Usedom (Heringsdorf)*/
    };


    public static String flightSearch(int delay, boolean getJson){

        try{
            return getJson(delay, getJson);
        } catch (Exception e){
            log.error("Error {}", e.getMessage());
            return "";
        }
    }

    private static String getJson(int minDelay, boolean getJson) throws Exception {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrb = Json.createArrayBuilder();

        Gson gson = new Gson();

        ArrayList<String> flights = new ArrayList<>();

        for (String airport : AIRPORTS) {
            InputStream inputStream = new URL(airport).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Datei als JSON-Objekt einlesen
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            JsonArray data = json.get("data").getAsJsonArray();


            for (JsonElement jsonElement : data) {
                JsonObject jO = (JsonObject) jsonElement;
                int ddelay = jO.get("DDELAY").getAsInt();

                if (ddelay > minDelay) {
                    String fc = cleanString(jO.get("FC").toString());
                    String daid = cleanString(jO.get("DAID").toString());
                    String aaid = cleanString(jO.get("AAID").toString());

                    JsonObjectBuilder flight = Json.createObjectBuilder();
                    flight.add("FLUG", fc);
                    flight.add("START", daid);
                    flight.add("ENDE", aaid);
                    flight.add("DELAY", ddelay);

                    flights.add("FLUG " + fc + " von " + daid + " nach " + aaid + " " + ddelay + " Minuten \n");

                    arrb.add(flight);

                }
            }
        }

        builder.add("data", arrb);

        if(getJson){
            return builder.build().toString();
        } else {
            return  flights.toString();
        }
    }

    private static String cleanString(String input){
        return input.replace("\"", "").replace("[", "").replace("]", "");
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static void versendeEMail(String inhalt) {
        try{
            Properties props = System.getProperties();

            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_MAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(SMTP_MAIL));
            message.setSubject("Flugverspaetung: " + dateFormat.format(new Date()), "UTF-8");
            message.setText(inhalt, "UTF-8");

            Transport transport = session.getTransport();
            transport.connect(SMTP_HOST, SMTP_MAIL, SMTP_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());

            Transport.send(message);
        } catch (Exception e){
            //ToDo: Fehler-Handling
        }
    }
}
