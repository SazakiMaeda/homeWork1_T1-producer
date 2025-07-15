package com.sazakimaeda.hm1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sazakimaeda.hm1.dto.WeatherDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String serverIpAddress;

    @Value("${spring.kafka.template.default-topic}")
    private String topic;

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Bean
    @SneakyThrows
    public ProducerFactory<String, WeatherDto> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        log.info("Попытка подключения к Kafka по адресу: {}", serverIpAddress);
        config.put(ProducerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverIpAddress);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, acks);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        log.info("Конфигурация Kafka Producer: {}", config);

        DefaultKafkaProducerFactory<String, WeatherDto> factory =
                new DefaultKafkaProducerFactory<>(config);
        factory.setValueSerializer(new JsonSerializer<>(kafkaObjectMapper()));
        return factory;
    }

    @Bean
    public KafkaTemplate<String, WeatherDto> kafkaTemplate(
            ProducerFactory<String, WeatherDto> producerFactory) {
        KafkaTemplate<String, WeatherDto> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(topic);
        return kafkaTemplate;
    }

    @Bean
    public ObjectMapper kafkaObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}