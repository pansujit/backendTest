package com.pitechplus.rcim.backoffice.dto.common;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by dgliga on 14.02.2017.
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneNumber {

    String countryCode;
    String nationalNumber;
    String phoneNumberName;

    public PhoneNumber clone() {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(this.countryCode);
        phoneNumber.setNationalNumber(this.nationalNumber);
        return phoneNumber;
    }
}
