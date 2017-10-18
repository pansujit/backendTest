package com.pitechplus.rcim.backoffice.data.enums;

public enum Protocol {
	ISO14443A_4("ISO14443A_4"),ISO14443B_4("ISO14443B_4"),ISO14443A_MIFARE("ISO14443A_MIFARE"),
	HITAG_1("HITAG_1"),LEGIC("LEGIC"),FELICA("FELICA"),HID_PROX("HID_PROX"),HID_ICLASS("HID_ICLASS"),
	AWID("AWID"),INDALA("INDALA"),ISO14443B_PRIME("ISO14443B_PRIME"),EM_410X("EM_410X"),INVALID("invalid");
	
    private String value;
    Protocol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
