package de.utils;

import de.ScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static de.config.Configuration.*;

public class MailUtils {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    public static void versendeEMail(String inhalt) {
        if(inhalt.isEmpty()) return;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            // Get the Session object.
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(SMTP_MAIL_FROM, SMTP_PASSWORD);
                        }
                    });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(SMTP_MAIL_FROM));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(SMTP_MAIL_TO));
                message.setSubject("Flugverspaetung: " + dateFormat.format(new Date()));
                message.setText(inhalt);
                Transport.send(message);
                log.info("Email erfolgreich verschickt");
            } catch (Exception e) {
                log.error("Error {}", e.getMessage());
            }
    }

}
