package com.pitechplus.rcim.backoffice.dto.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 14.02.2017.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

    String city;
    String country;
    String formattedAddress;
    String postalCode;
    String streetName;
    String streetNumber;

    public Address clone() {
        Address address = new Address();
        address.setCity(this.city);
        address.setCountry(this.country);
        address.setFormattedAddress(this.formattedAddress);
        address.setPostalCode(this.postalCode);
        address.setStreetName(this.streetName);
        address.setStreetNumber(this.streetNumber);
        return address;
    }
}
