package com.lab.datastreams.util;

import com.lab.datastreams.models.RegistrationEvent;
import com.lab.datastreams.models.SaleEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class TestDataPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TestDataGenerator testDataGenerator;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    @Autowired
    public TestDataPublisher(TestDataGenerator testDataGenerator) {
        this.testDataGenerator = testDataGenerator;

        // Hardcoded Kafka producer configurations
        Map<String, Object> producerConfigs = new HashMap<>();
        producerConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        producerConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        ProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(producerConfigs);
        this.kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    public void populateKafkaTopics(int runDurationInSeconds, int registrationIntervalInSeconds, int saleIntervalInSeconds, boolean generateInvalidData) {
        // Schedule the RegistrationEvent
        executorService.scheduleAtFixedRate(() -> {
            RegistrationEvent event = generateInvalidData ? testDataGenerator.generateInvalidRegistrationEvent() : testDataGenerator.generateValidRegistrationEvent();
            kafkaTemplate.send("TOPIC_A", null, event);
            System.out.println("Published RegistrationEvent: " + event);
        }, 0, registrationIntervalInSeconds, TimeUnit.SECONDS);

        // Schedule the SaleEvent
        executorService.scheduleAtFixedRate(() -> {
            SaleEvent event = generateInvalidData ? testDataGenerator.generateInvalidSaleEvent() : testDataGenerator.generateValidSaleEvent();
            kafkaTemplate.send("TOPIC_B", null, event);
            System.out.println("Published SaleEvent: " + event);
        }, 0, saleIntervalInSeconds, TimeUnit.SECONDS);

        // Shutdown after runDurationInSeconds
        executorService.schedule(() -> {
            executorService.shutdown();
            System.out.println("Executor service shutdown after " + runDurationInSeconds + " seconds.");
        }, runDurationInSeconds, TimeUnit.SECONDS);
    }
}





