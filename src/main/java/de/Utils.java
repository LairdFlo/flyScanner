package de;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.config.Configuration;
import de.data.Flight;
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

import static de.config.Configuration.*;

public class Utils {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    public static ArrayList<Flight> getDelayFlight(int minDelay) throws Exception {

        try {

            Gson gson = new Gson();

            ArrayList<Flight> flights = new ArrayList<>();

            for (String airport : Configuration.DATA) {
                InputStream inputStream = new URL(airport).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                JsonObject json = gson.fromJson(reader, JsonObject.class);
                JsonArray data = json.get("data").getAsJsonArray();

                for (JsonElement jsonElement : data) {

                    Flight flight =  getFlug((JsonObject) jsonElement);

                    if (flight.getDelay() > minDelay) {
                        flights.add(flight);
                    }
                }
            }
            return flights;
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
            return null;
        }
    }

    public static void versendeEMail(String inhalt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
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
            //message.setContent(inhalt, "text/html; charset=utf-8");

            Transport transport = session.getTransport();
            transport.connect(SMTP_HOST, SMTP_MAIL, SMTP_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());

            Transport.send(message);
        } catch (Exception e) {
            //ToDo: Fehler-Handling
        }
    }

    public static Flight getFlug(JsonObject jo) {

        Flight flight = new Flight();

        flight.setDelay(jo.get("DDELAY").getAsInt());
        flight.setFc(cleanString(jo.get("FC").toString()));
        flight.setDaid(cleanString(jo.get("DAID").toString()));
        flight.setAaid(cleanString(jo.get("AAID").toString()));
        flight.setSsd(cleanString(jo.get("SDD").toString()));
        flight.setAdd(cleanString(jo.get("ADD").toString()));
        flight.setAad(cleanString(jo.get("AAD").toString()));
        flight.setSad(cleanString(jo.get("SAD").toString()));

        return flight;
    }

    public static JsonObjectBuilder getFlightObjekt(Flight flug) {
        JsonObjectBuilder flight = Json.createObjectBuilder();
        flight.add("FLUG", flug.getFc());
        flight.add("START", flug.getDaid());
        flight.add("ENDE", flug.getAaid());
        flight.add("DELAY", flug.getDelay());
        return flight;
    }

    public static String getFlightJson(ArrayList<Flight> flights){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonArrayBuilder arrb = Json.createArrayBuilder();

        for (Flight flight : flights) {
            arrb.add(getFlightObjekt(flight));
        }

        builder.add("data", arrb);

        return builder.build().toString();

    }

    public static String getFlightString(ArrayList<Flight> flights){
        StringBuilder result = new StringBuilder();

        for (Flight flight : flights) {
            result.append("<br />" + flight.getFc() + " von " + flight.getDaid() + " nach " + flight.getAaid() + " " + flight.getDelay() + " Minuten (alt: " + flight.getSsd() + " | neu: " + flight.getAdd() + ")");
        }

        return result.toString();
    }

    private static String cleanString(String input) {
        return input.replace("\"", "").replace("[", "").replace("]", "");
    }




}
