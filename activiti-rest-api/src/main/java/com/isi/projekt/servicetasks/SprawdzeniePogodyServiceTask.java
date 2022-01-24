package com.isi.projekt.servicetasks;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class SprawdzeniePogodyServiceTask implements JavaDelegate {
    private Map<String, List<String>> mailsWithWeather;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            Map<String, String> mails = Optional.ofNullable(delegateExecution.getVariable("biegacze",Map.class)).orElse(Collections.emptyMap()); //map {key : mail, value : city }
            List<String> cities = new ArrayList<>();
            List<String> tempMails = new ArrayList<>();
            for (Map.Entry<String, String> entry : mails.entrySet()) {
                cities.add(entry.getValue());
                tempMails.add(entry.getKey());
            }
            mailsWithWeather = new HashMap<>();

            for (int i = 0; i < cities.size(); i++) {
                List<Weather> weatherObjects = new ArrayList<>();
                List<String> weatherCondiotions = new ArrayList<>();
                URL weatherUrl = new URL("https://goweather.herokuapp.com/weather/" + cities.get(i));
                HttpURLConnection httpURLConnection = (HttpURLConnection) weatherUrl.openConnection();
                httpURLConnection.setRequestMethod("GET");
                if (httpURLConnection.getResponseCode() != 200) {
                    throw new RuntimeException("Http runtime error: " + httpURLConnection.getResponseCode());
                }
                BufferedReader bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String output;
                StringBuilder response = new StringBuilder();
                while ((output = bf.readLine()) != null) {
                    response.append(output);
                }
                httpURLConnection.disconnect();
                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                JSONArray jsonArray = jsonObject.getJSONArray("forecast");
                for (int j = 0; j < jsonArray.length(); j++) {
                    Weather weather = new Weather();
                    weather.setNumOfDay(jsonArray.getJSONObject(j).getString("day"));
                    weather.setTemperature(jsonArray.getJSONObject(j).getString("temperature"));
                    weather.setWind(jsonArray.getJSONObject(j).getString("wind"));
                    System.out.println(weather.toString());
                    weatherObjects.add(weather);
                    weatherCondiotions.add(weather.toString());
                }
                mailsWithWeather.put(tempMails.get(i), weatherCondiotions);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        delegateExecution.setVariableLocal("pogoda", mailsWithWeather);
    }
}
