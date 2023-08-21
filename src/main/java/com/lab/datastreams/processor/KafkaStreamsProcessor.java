package com.lab.datastreams.processor;

import com.lab.datastreams.models.Key;
import com.lab.datastreams.models.RegistrationEvent;
import com.lab.datastreams.models.SaleEvent;
import com.lab.datastreams.models.SaleWrapperEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.stereotype.Component;

import static com.lab.datastreams.util.Validator.*;

@EnableKafkaStreams
@Component
@Slf4j
public class KafkaStreamsProcessor {

    @Autowired
    public void processStream(StreamsBuilder builder) {

            Serde<Key> customKeySerde = createJsonPOJOSerde(Key.class);
            Serde<RegistrationEvent> registrationEventSerde = createJsonPOJOSerde(RegistrationEvent.class);
            Serde<SaleEvent> saleEventSerde = createJsonPOJOSerde(SaleEvent.class);
            Serde<SaleWrapperEvent> saleWrapperEventJsonSerde = createJsonPOJOSerde(SaleWrapperEvent.class);

            // Explicit Serdes for TOPIC_A
            KStream<Key, RegistrationEvent> registrationEventKStream = builder.stream("TOPIC_A",
                            Consumed.with(customKeySerde, registrationEventSerde))
                    .filter((key, value) -> isValidFormat(value.getSelling_status_date())
                            && "001".equals(value.getCountry())
                            && isValidCatalogNumber(value.getCatalog_number()))
                    .peek((key, value) -> log.debug("Registration Event:: " + key + " - " + value))
                    .selectKey((key, value) -> value.getKey());

            // Materialized store for the KTable
            Materialized<Key, RegistrationEvent, KeyValueStore<Bytes, byte[]>> registrationMaterialized =
                    Materialized.<Key, RegistrationEvent, KeyValueStore<Bytes, byte[]>>as("registration-store")
                            .withKeySerde(customKeySerde)
                            .withValueSerde(registrationEventSerde);

            KTable<Key, RegistrationEvent> rekeyedRegistrationEventKTable =
                    registrationEventKStream.toTable(registrationMaterialized);

            // Explicit Serdes for TOPIC_B
            KStream<Key, SaleEvent> rekeySaleEventKStream = builder.stream("TOPIC_B",
                            Consumed.with(customKeySerde, saleEventSerde))
                    .filter((key, value) -> isValidFormat(value.getSales_date())
                            && "001".equals(value.getCountry())
                            && isValidCatalogNumber(value.getCatalog_number()))
                    .peek((key, value) -> log.debug("Sale Event :: " + key + " - " + value))
                    .selectKey((key, value) -> value.getKey());

            KStream<Key, SaleWrapperEvent> joinedStream = rekeySaleEventKStream
                    .join(
                            rekeyedRegistrationEventKTable,
                            (saleEvent, registrationEvent) -> new SaleWrapperEvent(registrationEvent, saleEvent),
                            Joined.with(customKeySerde, saleEventSerde, registrationEventSerde)
                    );

            joinedStream
                    .peek((key, value) -> log.debug("Joined record : " + key + " - " + value))
                    .to("TOPIC_C", Produced.with(customKeySerde, saleWrapperEventJsonSerde));
    }
}

