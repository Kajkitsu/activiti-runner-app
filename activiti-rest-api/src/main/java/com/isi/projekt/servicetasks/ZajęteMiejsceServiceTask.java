package com.isi.projekt.servicetasks;

import java.util.Collections;
import java.util.Optional;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class ZajęteMiejsceServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            List<String> mails = Optional.ofNullable(delegateExecution.getVariable("powtorzenia",List.class)).orElse(Collections.emptyList());
            String from = "koordynator.hdk@gmail.com";
            String theme = "Uwaga! Twoje zgłoszenie zostało wysłane już wcześniej";
            String host = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.required", "true");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("koordynator.hdk@gmail.com", "zaq1@WSX");
                }

            });
            // Used to debug SMTP issues
            session.setDebug(true);
            // Get the default Session object.

            try {
                for(int i = 0; i<mails.size(); i++){
                    // Create a default MimeMessage object.
                    MimeMessage message = new MimeMessage(session);
                    // Set From: header field of the header.
                    message.setFrom(new InternetAddress(from));
                    // Set To: header field of the header.
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(mails.get(i)));
                    // Set Subject: header field
                    message.setSubject(theme);
                    // Now set the actual message
                    message.setText("Witamy. Twoje zgłoszenie zostało już wcześniej wysłane, więc jesteś już zapisany na bieg.");
                    // Send message
                    Transport.send(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
