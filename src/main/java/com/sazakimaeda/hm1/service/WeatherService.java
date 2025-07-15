package com.sazakimaeda.hm1.service;

import com.sazakimaeda.hm1.dto.WeatherDto;
import com.sazakimaeda.hm1.producer.WeatherProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherProducer weatherProducer;
    private final Random RANDOM = new Random();
    private final List<String> CITY_NAME = List.of("Magadan", "Chukotka", "Saint-Petersburg", "Tyumen", "Moscow");
    private final List<String> WEATHER_STATUS = List.of("Солнечно", "Облачно", "Дождь");
    private int weekDays = 0;
    private int cityIndex = 0;

    @Scheduled(fixedRate = 2000)
    public void sendWeather() {
         WeatherDto weatherDto = WeatherDto.builder()
                .cityName(selectCity())
                .dailyWeather(List.of(WeatherDto.DailyWeather.builder()
                                .dateTime(LocalDate.now().plusDays(weekDays))
                                .status(WEATHER_STATUS.get(RANDOM.nextInt(WEATHER_STATUS.size())))
                                .temperature(RANDOM.nextInt(36))
                        .build()))
                 .timestamp(LocalDateTime.now())
                .build();
         weatherProducer.sendAsync(weatherDto);
    }

    @Scheduled(fixedRate = 14000)
    public void nextDayWeather() {
        weekDays++;
    }

    public String selectCity() {
        cityIndex = (cityIndex + 1) % CITY_NAME.size();
        return CITY_NAME.get(cityIndex);
    }
}
