package com.isi.projekt.servicetasks;

import com.isi.projekt.dto.RunnerDTO;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendUnconfirmEmailServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        RunnerDTO runnerDTO = RunnerDTO.from(execution.getVariable("runnerDTO", String.class));

        sendEmail(runnerDTO.getEmail(), runnerDTO.getName());

        execution.setVariable("runnerDTO", runnerDTO);
    }

    private void sendEmail(String email, String name) {
        StringBuilder finalText = new StringBuilder();
        finalText.append("Cześć, ").append(name).append("!").append("\n").append("\n");
        finalText.append("\tnie otrzymaliśmy płatności za bieg. Zostałes wykreślony z biegu").append("\n");
        finalText.append("\nPozdrawiamy, XYZ");

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("vpn.wat.isi@gmail.com"
                        , "UW3cGQuk]P+H9GAT");
            }

        });
        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("vpn.wat.isi@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Usunięcie z listy zawodników");
            message.setText(finalText.toString());
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
