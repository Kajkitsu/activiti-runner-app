package com.isi.projekt.dto;


public class WeatherDTO {
    private String numOfDay;
    private String temperature;
    private String wind;

    public WeatherDTO(String numOfDay, String temperature, String wind) {
        this.numOfDay = numOfDay;
        this.temperature = temperature;
        this.wind = wind;
    }

    public WeatherDTO() {

    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getNumOfDay() {
        return numOfDay;
    }

    public void setNumOfDay(String numOfDay) {
        this.numOfDay = numOfDay;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "numOfDay='" + numOfDay + '\'' +
                ", temperature='" + temperature + '\'' +
                ", wind='" + wind + '\'' +
                '}';
    }
}
