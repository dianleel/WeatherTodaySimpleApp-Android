package com.example.weather_lab;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "your_api-key_here";
    private WeatherApi weatherApi;
    private TextView weatherTextView;
    private ImageView weatherIcon;
    private EditText cityInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherTextView = findViewById(R.id.weather_text);
        weatherIcon = findViewById(R.id.weather_icon);
        cityInput = findViewById(R.id.city_input);
        Button searchButton = findViewById(R.id.search_button);

        TextView weatherTitle = findViewById(R.id.weather_title);
        String currentDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        weatherTitle.setText("Weather on " + currentDate);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherApi = retrofit.create(WeatherApi.class);

        searchButton.setOnClickListener(v -> {
            String city = cityInput.getText().toString().trim();
            if (!city.isEmpty()) {
                fetchWeather(city);
            }
        });
    }

    private void fetchWeather(String city) {
        Call<WeatherResponse> call = weatherApi.getCurrentWeather(city, API_KEY, "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();

                    String weatherInfo = "Temperature: " + weatherResponse.getMain().getTemp() + "°C\n" +
                            "Feels like: " + weatherResponse.getMain().getFeelsLike() + "°C\n" +
                            "Min Temp: " + weatherResponse.getMain().getTempMin() + "°C\n" +
                            "Max Temp: " + weatherResponse.getMain().getTempMax() + "°C\n" +
                            "Humidity: " + weatherResponse.getMain().getHumidity() + "%\n" +
                            "Pressure: " + weatherResponse.getMain().getPressure() + " hPa\n" +
                            "Wind Speed: " + weatherResponse.getWind().getSpeed() + " m/s\n" +
                            "Wind Direction: " + weatherResponse.getWind().getDeg() + "°\n" +
                            "Cloudiness: " + weatherResponse.getClouds().getAll() + "%\n" +
                            "Description: " + weatherResponse.getWeather().get(0).getDescription();

                    weatherTextView.setText(weatherInfo);

                    String weatherDescription = weatherResponse.getWeather().get(0).getDescription().toLowerCase();
                    if (weatherDescription.contains("clear") || weatherDescription.contains("sun")) {
                        weatherIcon.setImageResource(R.drawable.sun);
                    } else if (weatherDescription.contains("cloud")) {
                        weatherIcon.setImageResource(R.drawable.cloudy);
                    } else if (weatherDescription.contains("rain")) {
                        weatherIcon.setImageResource(R.drawable.rain);
                    } else if (weatherDescription.contains("snow")) {
                        weatherIcon.setImageResource(R.drawable.snow);
                    } else if (weatherDescription.contains("storm") || weatherDescription.contains("gale")) {
                        weatherIcon.setImageResource(R.drawable.gale);
                    } else {
                        weatherIcon.setImageResource(R.drawable.cloudy);
                    }
                } else {
                    Log.e("MainActivity", "Error: " + response.errorBody());
                    weatherTextView.setText("Error loading data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                Log.e("MainActivity", "Request failed", t);
                weatherTextView.setText("Network error");
            }
        });
    }
}