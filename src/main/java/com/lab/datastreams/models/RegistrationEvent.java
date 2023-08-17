package com.lab.datastreams.models;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a registration event.
 * <p>
 * JSON representation:
 * <pre>
 * {
 *   "key": {
 *     "catalog_number": "29525",
 *     "country": "001"
 *   },
 *   "is_selling": true,
 *   "model": "29525",
 *   "product_id": "int7218",
 *   "registration_id": "int4123",
 *   "registration_number": "REG03814",
 *   "selling_status_date": "2023-06-30T18:21:31.000000Z",
 *   "country": "001"
 *   // ...
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RegistrationEvent {
    private CustomKey key;
    private boolean is_selling;

    private String catalog_number;
    private String model;
    private String product_id;
    private String registration_id;
    private String registration_number;
    private LocalDateTime selling_status_date;
    private String country;
}
