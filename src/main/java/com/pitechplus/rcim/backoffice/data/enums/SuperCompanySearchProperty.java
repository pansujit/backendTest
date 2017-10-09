package com.pitechplus.rcim.backoffice.data.enums;

/**
 * Created by dgliga on 13.06.2017.
 */
public enum SuperCompanySearchProperty {

    NAME("NAME");

    private String value;

    SuperCompanySearchProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
