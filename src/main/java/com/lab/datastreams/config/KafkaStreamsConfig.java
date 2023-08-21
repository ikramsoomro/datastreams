package com.lab.datastreams.config;


import com.lab.datastreams.util.JsonPOJODeserializer;
import com.lab.datastreams.util.JsonPOJOSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-app");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        properties.put("spring.kafka.producer.key-serializer", StringSerializer.class.getName());
        properties.put("spring.kafka.producer.value-serializer", JsonPOJOSerializer.class.getName());
        properties.put("spring.kafka.consumer.key-deserializer", StringDeserializer.class.getName());
        properties.put("spring.kafka.consumer.value-deserializer", JsonPOJODeserializer.class.getName());
        properties.put("spring.kafka.consumer.auto-offset-reset", "earliest");
        properties.put("spring.json.trusted.packages", "com.lab.datastreams.models");
        properties.put("spring.kafka.consumer.properties.spring.json.trusted.packages", "com.lab.datastreams.models");
        properties.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                LogAndContinueExceptionHandler.class.getName());

        return new KafkaStreamsConfiguration(properties);
    }
}

