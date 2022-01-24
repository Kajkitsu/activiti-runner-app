package com.isi.projekt.servicetasks;

import com.isi.projekt.dto.RunnerDTO;
import com.isi.projekt.dto.WeatherDTO;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Properties;

public class SendPaymentEmailServiceTask implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        RunnerDTO runnerDTO = RunnerDTO.from(execution.getVariable("runnerDTO", String.class));

        sendEmail(runnerDTO.getPaymentLink(), runnerDTO.getEmail(), runnerDTO.getName(), runnerDTO.getWeatherDTO());

        execution.setVariable("runnerDTO", runnerDTO.toJson());
    }

    private void sendEmail(String paylink, String email, String name, WeatherDTO weatherDTO) {
        StringBuilder finalText = new StringBuilder();
        finalText.append("Cześć, ").append(name).append("!").append("\n").append("\n");
        finalText.append("\tprzesyłamy link do twojej płatności: ").append(paylink).append("\n");
        finalText.append("\tprzewidywana pogoda: ").append(Optional.ofNullable(weatherDTO).map(WeatherDTO::toString).orElse(""));
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
            message.setSubject("Link do płatności biegu i prognoza pogody");
            message.setText(finalText.toString());
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
