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
        LocalDateTime dateTime = LocalDateTime.now();

        String catalog = String.valueOf(random.nextInt(100000));  // assuming valid catalog is 5 digits
        String countryCode = "001";
        String registrationType = "REG" + random.nextInt(10000);

        // Randomly choose a field to invalidate
        int choice = random.nextInt(9);  // 0 to 8 inclusive

        switch (choice) {
            case 0:
                catalog = String.valueOf(random.nextInt(1000)); // Catalog number with less than 5 digits
                break;
            case 1:
                countryCode = INVALID_COUNTRY_CODE;
                break;
            case 2:
                registrationType = "INVALID" + random.nextInt(10000);
                break;
            // ... Add invalidation for other fields similarly ...
            default:
                // Can default to any invalidation or leave as is
                break;
        }

        return new RegistrationEvent(
                new Key(catalog, countryCode),
                true,
                catalog,
                catalog,
                "int" + random.nextInt(10000),
                "int" + random.nextInt(10000),
                registrationType,
                dateTime.format(DATE_FORMATTER),
                countryCode,
                new Audit("Registration", "RGR")
        );
    }

    public SaleEvent generateInvalidSaleEvent() {
        LocalDateTime dateTime = LocalDateTime.now();

        String catalog = String.valueOf(random.nextInt(100000));  // assuming valid catalog is 5 digits
        String countryCode = "001";

        // Randomly choose a field to invalidate
        int choice = random.nextInt(6);  // 0 to 5 inclusive

        switch (choice) {
            case 0:
                catalog = String.valueOf(random.nextInt(1000)); // Catalog number with less than 5 digits
                break;
            case 1:
                countryCode = INVALID_COUNTRY_CODE;
                break;
            case 2:
                dateTime = null; // Invalid date
                break;
            // ... Add invalidation for other fields similarly ...
            default:
                // Can default to any invalidation or leave as is
                break;
        }

        return new SaleEvent(
                new Key(catalog, countryCode),
                catalog,
                String.valueOf(random.nextInt(10000)),
                String.valueOf(random.nextInt(100)),
                dateTime == null ? "invalidDateTimeFormat" : dateTime.format(DATE_FORMATTER),
                countryCode,
                new Audit("Sales Event", "SLS")
        );
    }

    private String getRandomCatalogCode() {
        return CATALOG_CODES[ThreadLocalRandom.current().nextInt(0, CATALOG_CODES.length)];
    }
}


