package de;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.data.Flug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import static de.Utils.*;

public class FlightChecker {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    public static ArrayList<Flug> getDelayFlight(int minDelay) throws Exception {

        try {

            Gson gson = new Gson();

            ArrayList<Flug> flights = new ArrayList<>();

            for (String airport : Konfig.DATA) {
                InputStream inputStream = new URL(airport).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                JsonObject json = gson.fromJson(reader, JsonObject.class);
                JsonArray data = json.get("data").getAsJsonArray();

                for (JsonElement jsonElement : data) {

                    Flug flug =  getFlug((JsonObject) jsonElement);

                    if (flug.getDelay() > minDelay) {
                        flights.add(flug);
                    }
                }
            }
            return flights;
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
            return null;
        }
    }


    private static String getDate(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        long timeLong = Long.valueOf(time);

        Date date = new Date();
        date.setTime(timeLong * 1000);

        return dateFormat.format(date);
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

}
