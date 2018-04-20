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

    public static String flightSearch(int delay, boolean getJson) {

        try {
            return getJson(delay, getJson);
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
            return "";
        }
    }

    private static String getJson(int minDelay, boolean getJson) throws Exception {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrb = Json.createArrayBuilder();

        Gson gson = new Gson();

        ArrayList<String> flights = new ArrayList<>();

        InputStream inputStream = new URL(EUROWINGS_FLIGHTS).openStream();
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

        builder.add("data", arrb);

        if (getJson) {
            return builder.build().toString();
        } else {
            return flights.toString();
        }
    }

    private static String cleanString(String input) {
        return input.replace("\"", "").replace("[", "").replace("]", "");
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static void versendeEMail(String inhalt) {
        try {
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
        } catch (Exception e) {
            //ToDo: Fehler-Handling
        }
    }
}
