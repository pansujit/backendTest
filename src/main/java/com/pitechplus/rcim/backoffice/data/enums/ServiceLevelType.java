package com.pitechplus.rcim.backoffice.data.enums;

/**
 * Created by dgliga on 18.07.2017.
 */
public enum ServiceLevelType {
    BASIC, GOLD, PREMIUM;

    public static String getFieldNameInClass() {
        return "serviceLevelType";
    }
}
