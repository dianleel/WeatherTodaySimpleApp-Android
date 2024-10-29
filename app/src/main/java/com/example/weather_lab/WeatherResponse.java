package com.example.weather_lab;

import java.util.List;

public class WeatherResponse {
    private Main main;
    private List<Weather> weather;
    private Wind wind;
    private Clouds clouds;

    public Main getMain() { return main; }
    public List<Weather> getWeather() { return weather; }
    public Wind getWind() { return wind; }
    public Clouds getClouds() { return clouds; }

    public static class Main {
        private double temp;
        private double feels_like;  // Ощущаемая температура
        private double temp_min;    // Минимальная температура
        private double temp_max;    // Максимальная температура
        private int pressure;
        private int humidity;

        public double getTemp() { return temp; }
        public double getFeelsLike() { return feels_like; }
        public double getTempMin() { return temp_min; }
        public double getTempMax() { return temp_max; }
        public int getPressure() { return pressure; }
        public int getHumidity() { return humidity; }
    }

    public static class Weather {
        private String description;

        public String getDescription() { return description; }
    }

    public static class Wind {
        private double speed;
        private int deg;

        public double getSpeed() { return speed; }
        public int getDeg() { return deg; }
    }

    public static class Clouds {
        private int all; // Процент облачности

        public int getAll() { return all; }
    }
}