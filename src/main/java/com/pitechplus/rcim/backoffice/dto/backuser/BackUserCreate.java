package com.pitechplus.rcim.backoffice.dto.backuser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pitechplus.rcim.backoffice.dto.common.Address;
import com.pitechplus.rcim.backoffice.dto.common.PhoneNumber;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by dgliga on 27.02.2017.
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class BackUserCreate {

    UUID id;
    String login;
    String password;
    String firstName;
    String lastName;
    PhoneNumber phoneNumber;
    String role;
    Address address;
    Locale locale;
    UUID companyId;

    public BackUserCreate clone() {
        BackUserCreate backUserCreate = new BackUserCreate();
        backUserCreate.setId(this.id);
        backUserCreate.setAddress(this.address.clone());
        backUserCreate.setPhoneNumber(this.phoneNumber.clone());
        backUserCreate.setFirstName(this.firstName);
        backUserCreate.setLastName(this.lastName);
        backUserCreate.setLocale(this.locale);
        backUserCreate.setLogin(this.login);
        backUserCreate.setRole(this.role);
        return backUserCreate;
    }
}
