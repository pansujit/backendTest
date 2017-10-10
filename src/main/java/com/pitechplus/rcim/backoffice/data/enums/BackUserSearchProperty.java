package com.pitechplus.rcim.backoffice.data.enums;

/**
 * Created by dgliga on 08.03.2017.
 */
public enum BackUserSearchProperty {
    FIRSTNAME("firstName"), LASTNAME("lastName"), EMAIL("login"), ROLE("role"), ENABLED("enabled"),VIP("vip");

    private String value;

    BackUserSearchProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
