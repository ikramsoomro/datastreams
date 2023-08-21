package com.lab.datastreams.util;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateTimeValidator {

    private static final Logger logger = Logger.getLogger(DateTimeValidator.class.getName());

    public static boolean isValidFormat(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX");

        try {
            formatter.parse(input);
            return true;
        } catch (DateTimeParseException e) {
            logger.log(Level.WARNING, "Invalid date-time format: " + input, e);
            return false;
        }
    }
}

