package com.lab.datastreams.models;

import lombok.*;

import java.time.LocalDateTime;

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
public class SaleEvent {
    private CustomKey key;

    private String catalog_number;
    private String order_number;
    private String quantity;
    private LocalDateTime sales_date;
    private String country;
}
