package com.isi.projekt.servicetasks;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PobranieUczestnikowServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            URL runnersURL = new URL("http://localhost:5001/runners");
            HttpURLConnection connection = (HttpURLConnection) runnersURL.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Http runtime error: " + connection.getResponseCode());
            }
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String output;
            StringBuilder response = new StringBuilder();
            while ((output = bf.readLine()) != null) {
                response.append(output);
            }
            connection.disconnect();
            Map<Integer, String> allEmails = new HashMap<>();
            Map<Integer, String> cities = new HashMap<>();
            JSONArray jsonEmails = new JSONArray(String.valueOf(response));
            for (int i = 0; i < jsonEmails.length(); i++) {
                allEmails.put(i + 1,jsonEmails.getJSONObject(i).getString("email"));
                cities.put(i + 1, jsonEmails.getJSONObject(i).getString("city"));
            }
            System.out.println("Maile: " + allEmails.toString());
            System.out.println("Miasta: " + cities.toString());
            delegateExecution.setVariable("maile", allEmails);
            delegateExecution.setVariable("miasta", cities);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
