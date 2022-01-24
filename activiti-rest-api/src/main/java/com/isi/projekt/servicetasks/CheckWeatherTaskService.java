package com.isi.projekt.servicetasks;

import com.isi.projekt.dto.RunnerDTO;
import com.isi.projekt.dto.WeatherDTO;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Optional;

public class CheckWeatherTaskService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        RunnerDTO runnerDTO = RunnerDTO.from(execution.getVariable("runnerDTO", String.class));

        getWeather(runnerDTO.getCity())
                .ifPresent(runnerDTO::setWeatherDTO);

        execution.setVariable("runnerDTO", runnerDTO.toJson());

    }

    private Optional<WeatherDTO> getWeather(String city) { //TODO
        String url = "https://goweather.herokuapp.com/weather/" + city;
        HttpGet request = new HttpGet(url);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        try {
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(request);
            JSONObject jsonObject = new JSONObject(EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8"));
            JSONArray jsonArray = jsonObject.getJSONArray("forecast");

            WeatherDTO weatherDTO = new WeatherDTO();
            weatherDTO.setNumOfDay(jsonArray.getJSONObject(0).getString("day"));
            weatherDTO.setTemperature(jsonArray.getJSONObject(0).getString("temperature"));
            weatherDTO.setWind(jsonArray.getJSONObject(0).getString("wind"));
            return Optional.of(weatherDTO);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
