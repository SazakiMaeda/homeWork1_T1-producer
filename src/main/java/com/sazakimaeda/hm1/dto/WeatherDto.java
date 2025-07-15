package com.sazakimaeda.hm1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {

    @JsonProperty("city")
    private String cityName;

    @JsonProperty("weather")
    private List<DailyWeather> dailyWeather;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyWeather {

        @JsonProperty("time")
        private LocalDate dateTime;

        @JsonProperty("temperature")
        private int temperature;

        @JsonProperty("status")
        private String status;
    }
}

