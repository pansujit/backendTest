package com.pitechplus.rcim.nissan.be.nissandata.nissanenums;

/**
 * Created by dgliga  on 07.09.2017.
 */

public enum GroupPropertyDto {
    PUBLIC_ID("publicId"),
    GROUP_SIZE("groupSize"),
    SITE_NAME("siteName"),
    START_DATE("groupStartDate"),
    END_DATE("groupEndDate"),
    GROUP_STATE("groupState");

    private String value;

    GroupPropertyDto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

