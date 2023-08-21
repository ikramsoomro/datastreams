package com.lab.datastreams.models;

import lombok.*;

import java.io.Serializable;

/**
 * Represents a wrapper event containing both registration and sale events.
 * <p>
 * JSON representation:
 * <pre>
 * {
 *   "key": {
 *     "catalog_number": "29525",
 *     "country": "001"
 *   },
 *   "registrationEvent": {
 *     // JSON representation of RegistrationEvent
 *   },
 *   "saleEvent": {
 *     // JSON representation of SaleEvent
 *   }
 * }
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SaleWrapperEvent implements Serializable {
    private RegistrationEvent registrationEvent;
    private SaleEvent saleEvent;

}
