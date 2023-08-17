package com.lab.datastreams.processor;

import com.lab.datastreams.models.CustomKey;
import com.lab.datastreams.models.RegistrationEvent;
import com.lab.datastreams.models.SaleEvent;
import com.lab.datastreams.models.SaleWrapperEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@EnableKafkaStreams
@Component
@Slf4j
public class KafkaStreamsProcessor {

    @Autowired
    public void processStream(StreamsBuilder builder) {

        // Create KStream from the input topics using custom Serdes
        KTable<CustomKey, RegistrationEvent> topicAKTable = builder.table("TOPIC_A",
                Consumed.with(new JsonSerde<>(CustomKey.class), new JsonSerde<>(RegistrationEvent.class))
        ).filter((key, value) -> "001".equals(value.getCountry()) && isValidCatalogNumber(value.getCatalog_number()));

        KStream<CustomKey, SaleEvent> topicBStream = builder.stream("TOPIC_B",
                Consumed.with(new JsonSerde<>(CustomKey.class), new JsonSerde<>(SaleEvent.class)));

        // Join RegistrationEvent with SaleEvent using leftJoin
        KStream<CustomKey, SaleWrapperEvent> joinedStream = topicBStream
                .filter((key, value) -> "001".equals(value.getCountry()) && isValidCatalogNumber(value.getCatalog_number()))
                .leftJoin(topicAKTable,
                 (sale, registration) -> new SaleWrapperEvent(sale.getKey(), registration, sale));


        // Publish the joined stream to the output topic
        joinedStream.to("TOPIC_C");
    }

    private boolean isValidCatalogNumber(String catalogNumber) {
        return catalogNumber != null && catalogNumber.length() == 5;
    }
}

