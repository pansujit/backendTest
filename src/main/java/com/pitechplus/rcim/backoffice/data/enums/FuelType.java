package com.pitechplus.rcim.backoffice.data.enums;

/**
 * Created by dgliga on 18.07.2017.
 */
public enum FuelType {
    ELECTRIC, PETROL, DIESEL, HYBRID, LPG;

    public static String getFieldNameInClass() {
        return "fuelType";
    }
}
