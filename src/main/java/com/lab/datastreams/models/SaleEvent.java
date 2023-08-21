package com.lab.datastreams.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;

/**
 * Represents a sale event.
 * <p>
 * JSON representation:
 * <pre>
 * {
 *   "key": {
 *     "catalog_number": "29525",
 *     "country": "001"
 *   },
 *   "order_number": "03814",
 *   "quantity": "2",
 *   "sales_date": "2023-07-30T18:21:31.000000Z",
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
@ToString
@JsonSerialize
@JsonDeserialize
public class SaleEvent implements Serializable {
    private Key key;

    private String catalog_number;

    private String order_number;
    private String quantity;

    private String sales_date;
    private String country;

    private Audit auditl;
}
