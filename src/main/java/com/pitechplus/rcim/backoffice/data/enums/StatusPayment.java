package com.pitechplus.rcim.backoffice.data.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by dgliga on 21.08.2017.
 */
public enum StatusPayment {
    PAID("PAID"),
    PARTIALLY_PAID("PARTIALLY PAID"),
    UNPAID("UNPAID"),
    NA("N/A");

    private final String value;

    StatusPayment(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }

}
