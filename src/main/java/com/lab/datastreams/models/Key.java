package com.lab.datastreams.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;

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
@ToString
public class Key implements Serializable {

    private String catalog_number;
    private String country;
}
