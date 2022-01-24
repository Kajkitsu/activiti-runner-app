package com.isi.projekt.servicetasks;

import lombok.SneakyThrows;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class ZakupPakietuServiceTask implements JavaDelegate {

    @SneakyThrows
    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            List<String> emails = Optional.ofNullable(delegateExecution.getVariable("emails",List.class)).orElse(Collections.emptyList());
            System.out.println("Maile niepowtarzające się: " + emails.toString());
            List<String> typesOfRace = new ArrayList<>();
            typesOfRace.add("standard");
            typesOfRace.add("classic");
            typesOfRace.add("ultra");
            List<Race> raceList = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            for (String email : emails) {
                calendar.add(Calendar.DATE, 1);
                Date date = calendar.getTime();
                int random = new Random().nextInt(2);
                Race race = new Race();
                race.setEmail(email);
                race.setTyp_biegu(typesOfRace.get(random));
                race.setData_biegu(convert(date));
                race.setCena_pakietu(new Random().nextInt(100) + 150);
                raceList.add(race);
            }
            String raceString = raceList.toString();
            String prepareTojson = raceString.replace("Race", "");
            String nextPrep = prepareTojson.replace("[", "{ data : [");
            nextPrep = nextPrep.concat("}");
            JSONObject jObj = new JSONObject(nextPrep);
            FileWriter fileWriter = new FileWriter("race.json");
            fileWriter.write(String.valueOf(jObj));
            fileWriter.close();

            final String CLIENT_ID = "1000.Q7IF8A847V6R1577HQ8MJGEIHSC3HY";
            final String CLIENT_SECRET = "3f3db9377b1d21febad188980eaf8aa490e1af0ebe";
            final String CLIENT_CODE = "1000.d2e68a744239e6a1b24c583b8d34013d.87adfab82837a9d0dcb3f8332013472d"; //from zaho api console
            String authUrl = "https://accounts.zoho.eu/oauth/v2/token?grant_type=authorization_code&client_id=" + CLIENT_ID + "&client_secret="
                    + CLIENT_SECRET + "&code=" + CLIENT_CODE + "";
            HttpPost post = new HttpPost(authUrl);
            CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(post);
            HttpEntity entity = closeableHttpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");

            sendZohoPost(access_token);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendZohoPost(String access_token) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        String zohoUrl = "https://creator.zoho.eu/api/v2/activityasppp/activitysssss/form/Formularz";
        HttpPost httpRequest = new HttpPost(zohoUrl);
        httpRequest.addHeader("Authorization", "Zoho-oauthtoken " + access_token);
        httpRequest.addHeader("Content-type", "application/json");
        String data = FileUtils.readFileToString( Paths.get("race.json").toFile(), "UTF-8");
        StringEntity entity = new StringEntity(data);
        httpRequest.setEntity(entity);
        CloseableHttpResponse response = client.execute(httpRequest);
        client.close();

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Erorr: " + response.getStatusLine().getStatusCode());
        }
    }

    public String convert(Date date) {
        String newFormat = "";
        List<String> acronyms = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        Map<Integer, String> months = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            months.put(i, acronyms.get(i - 1));
        }
        date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        String aaa = strDate.substring(3, 5);
        int nOfMonth = Integer.parseInt(aaa);
        for (Map.Entry<Integer, String> entry : months.entrySet()) {
            if (nOfMonth == entry.getKey()) {
                newFormat = strDate.replace(strDate.substring(3, 5), entry.getValue());
            }
        }
        System.out.println(newFormat);
        return newFormat;
    }

}
