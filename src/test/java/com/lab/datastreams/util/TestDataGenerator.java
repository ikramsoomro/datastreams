package com.lab.datastreams.util;

import com.lab.datastreams.models.Audit;
import com.lab.datastreams.models.Key;
import com.lab.datastreams.models.RegistrationEvent;
import com.lab.datastreams.models.SaleEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class TestDataGenerator {

    private static final String[] CATALOG_CODES = {"12345", "67890", "11223"};
    private static final String COUNTRY_CODE = "001";
    private static final String INVALID_COUNTRY_CODE = "XYZ";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

    private final Random random = new Random();

    public RegistrationEvent generateValidRegistrationEvent() {
        String catalog = getRandomCatalogCode();
        LocalDateTime dateTime = LocalDateTime.now();

        return new RegistrationEvent(
                new Key(catalog, COUNTRY_CODE),
                true,
                catalog,
                catalog,
                "int" + random.nextInt(10000),
                "int" + random.nextInt(10000),
                "REG" + random.nextInt(10000),
                dateTime.format(DATE_FORMATTER),
                COUNTRY_CODE,
                new Audit("Registration", "RGR")
        );
    }

    public SaleEvent generateValidSaleEvent() {
        String catalog = getRandomCatalogCode();
        LocalDateTime dateTime = LocalDateTime.now();

        return new SaleEvent(
                new Key(catalog, COUNTRY_CODE),
                catalog,
                String.valueOf(random.nextInt(10000)),
                String.valueOf(random.nextInt(100)),
                dateTime.format(DATE_FORMATTER),
                COUNTRY_CODE,
                new Audit("Sales Event", "SLS")
        );
    }

    public RegistrationEvent generateInvalidRegistrationEvent() {
        String invalidCatalog = String.valueOf(random.nextInt(1000));  // Catalog number with less than 5 digits
        LocalDateTime dateTime = LocalDateTime.now();

        return new RegistrationEvent(
                new Key(invalidCatalog, INVALID_COUNTRY_CODE),
                true,
                invalidCatalog,
                invalidCatalog,
                "int" + random.nextInt(10000),
                "int" + random.nextInt(10000),
                "REG" + random.nextInt(10000),
                dateTime.format(DATE_FORMATTER),
                INVALID_COUNTRY_CODE,
                new Audit("Registration", "RGR")
        );
    }

    public SaleEvent generateInvalidSaleEvent() {
        String invalidCatalog = String.valueOf(random.nextInt(1000));  // Catalog number with less than 5 digits
        String invalidDateTime = "invalidDateTimeFormat";
        LocalDateTime dateTime = LocalDateTime.now();

        return new SaleEvent(
                new Key(invalidCatalog, INVALID_COUNTRY_CODE),
                invalidCatalog,
                String.valueOf(random.nextInt(10000)),
                String.valueOf(random.nextInt(100)),
                dateTime.format(DATE_FORMATTER),
                INVALID_COUNTRY_CODE,
                new Audit("Sales Event", "SLS")
        );
    }

    private String getRandomCatalogCode() {
        return CATALOG_CODES[ThreadLocalRandom.current().nextInt(0, CATALOG_CODES.length)];
    }
}


