package com.lab.datastreams.models;

import lombok.*;

/**
 * Represents a custom key used for joining events.
 * <p>
 * JSON representation:
 * <pre>
 * {
 *   "catalog_number": "29525",
 *   "country": "001"
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CustomKey {
    private String catalog_number;
    private String country;
}
