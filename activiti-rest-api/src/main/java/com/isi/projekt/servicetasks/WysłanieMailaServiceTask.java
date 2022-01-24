package com.isi.projekt.servicetasks;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class WysłanieMailaServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            List<String> emails = new ArrayList<>();
            Map<String, List<String>> weather = Optional.ofNullable(delegateExecution.getVariable("pogoda",Map.class)).orElse (Collections.emptyMap());

            StringBuilder mailText = new StringBuilder();
            StringBuilder finalText = new StringBuilder();
            for (Map.Entry<String, List<String>> entry : weather.entrySet()) {
                emails.add(entry.getKey());
            }

            for(Map.Entry<String, List<String>> entry: weather.entrySet()){
                finalText.setLength(0);
                mailText.setLength(0);
                mailText.append(entry.getValue());
                mailText.replace(0,8,"[");
                String textAsJson = String.valueOf(mailText);
                String text = textAsJson.replace(" Weather", "");
                System.out.println(text);
                JSONArray array = new JSONArray(text);
                for(int i =0; i<array.length(); i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    finalText.append("Dzień nr ").append(jsonObject.getString("numOfDay")).append("\n");
                    finalText.append("Temperatura: ").append(jsonObject.getString("temperature")).append("\n");
                    finalText.append("Wiatr: ").append(jsonObject.getString("wind")).append("\n");
                }
                finalText.append("Wybierz dogodny dla siebie dzień weź udział w wirtualnym biegu!");
                String from = "koordynator.hdk@gmail.com";
                String theme = "Informacje o pogodzie na najbliższe 3 dni";

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
                        return new PasswordAuthentication("koordynator.hdk@gmail.com"
                                , "zaq1@WSX");
                    }

                });
                // Used to debug SMTP issues
                session.setDebug(true);
                // Get the default Session object.
                try {
                    // Create a default MimeMessage object.
                    MimeMessage message = new MimeMessage(session);
                    // Set From: header field of the header.
                    message.setFrom(new InternetAddress(from));
                    // Set To: header field of the header.
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(entry.getKey()));
                    // Set Subject: header field
                    message.setSubject(theme);
                    // Now set the actual message
                    message.setText(String.valueOf(finalText));
                    // Send message
                    Transport.send(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            delegateExecution.setVariable("emails", emails);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
