package com.sazakimaeda.hm1.producer;

import com.sazakimaeda.hm1.dto.WeatherDto;
import com.sazakimaeda.hm1.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherProducer {

    private final KafkaTemplate<String, WeatherDto>  kafkaTemplate;

    public void sendAsync(WeatherDto weatherDto) {
        kafkaTemplate.sendDefault(weatherDto);
        log.info("Сообщение отправлено: {}", weatherDto);
    }
}
