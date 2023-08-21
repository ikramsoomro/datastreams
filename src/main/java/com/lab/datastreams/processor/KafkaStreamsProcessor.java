package com.lab.datastreams.processor;

import com.lab.datastreams.models.Key;
import com.lab.datastreams.models.RegistrationEvent;
import com.lab.datastreams.models.SaleEvent;
import com.lab.datastreams.models.SaleWrapperEvent;
import com.lab.datastreams.util.JsonPOJODeserializer;
import com.lab.datastreams.util.JsonPOJOSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.lab.datastreams.util.DateTimeValidator.isValidFormat;
import static org.apache.logging.log4j.ThreadContext.peek;

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
                    .peek((key, value) -> System.out.println(key + " - " + value))
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
                    .peek((key, value) -> System.out.println(key + " - " + value))
                    .filter((key, value) -> isValidFormat(value.getSales_date())
                            && "001".equals(value.getCountry())
                            && isValidCatalogNumber(value.getCatalog_number()))
                    .selectKey((key, value) -> value.getKey());

            KStream<Key, SaleWrapperEvent> joinedStream = rekeySaleEventKStream
                    .join(
                            rekeyedRegistrationEventKTable,
                            (saleEvent, registrationEvent) -> {
                                SaleWrapperEvent saleWrapperEvent = new SaleWrapperEvent(registrationEvent, saleEvent);
                                return saleWrapperEvent;
                            },
                            Joined.with(customKeySerde, saleEventSerde, registrationEventSerde)
                    );

            joinedStream
                    .peek((key, value) -> System.out.println("Joined Stream: " + key + " - " + value))
                    .to("TOPIC_C", Produced.with(customKeySerde, saleWrapperEventJsonSerde));
    }

    private boolean isValidCatalogNumber(String catalogNumber) {
        return catalogNumber != null && catalogNumber.length() == 5;
    }

    public <T> Serde<T> createJsonPOJOSerde(Class<T> tClass) {
        Serializer<T> serializer = new JsonPOJOSerializer<>();
        Deserializer<T> deserializer = new JsonPOJODeserializer<>(tClass);
        return Serdes.serdeFrom(serializer, deserializer);
    }


}

