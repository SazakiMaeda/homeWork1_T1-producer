package com.sazakimaeda.hm1;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@SpringBootApplication
@EnableScheduling
public class Hm1Application {

    public static void main(String[] args) {
        kafka.start();

        SpringApplication.run(Hm1Application.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(kafka::stop));
    }

    private static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("apache/kafka:latest")
    )
            .withCreateContainerCmdModifier(cmd -> cmd
                    .withPortBindings(new PortBinding(
                            Ports.Binding.bindPort(9094),
                            new ExposedPort(9092)
                    )));
}
