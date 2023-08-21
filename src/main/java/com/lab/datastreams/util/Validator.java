package com.lab.datastreams.util;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Validator {

    private static final Logger logger = Logger.getLogger(Validator.class.getName());

    public static boolean isValidFormat(String input) {
        if (input == null) {
            input = "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX");

        try {
            formatter.parse(input);
            return true;
        } catch (DateTimeParseException e) {
            logger.log(Level.WARNING, "Invalid date-time format: " + input, e);
            return false;
        }
    }

    public static boolean isValidCatalogNumber(String catalogNumber) {
        return catalogNumber != null && catalogNumber.length() == 5;
    }

    public static  <T> Serde<T> createJsonPOJOSerde(Class<T> tClass) {
        Serializer<T> serializer = new JsonPOJOSerializer<>();
        Deserializer<T> deserializer = new JsonPOJODeserializer<>(tClass);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}

