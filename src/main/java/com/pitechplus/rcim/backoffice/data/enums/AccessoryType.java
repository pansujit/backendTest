package com.pitechplus.rcim.backoffice.data.enums;

import com.pitechplus.qautils.randomgenerators.NumberGenerator;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dgliga on 18.07.2017.
 */
public enum AccessoryType {
    AIR_CONDITIONING, LARGE_TRUCK, FOLDING_SEATS, RADIO_CD, BLUETOOTH, GPS;

    public static Set<AccessoryType> getRandomAccessories() {
        Set<AccessoryType> accessoryTypes = new HashSet<>();
        for (int i = 0; i < NumberGenerator.randInt(0, AccessoryType.class.getEnumConstants().length); i++) {
            AccessoryType randomAccessory = DtoBuilders.randomEnum(AccessoryType.class);
            if (!accessoryTypes.contains(randomAccessory)) {
                accessoryTypes.add(randomAccessory);
            }
        }
        return accessoryTypes;
    }
}
