package com.pitechplus.rcim.backoffice.data.enums;

public enum SmartCardSortProperty {
	CARD_ID("cardId"), COMPANY_ID("companyId"), USER_LOGIN("userLogin"), RFID_PROTOCOL("protocol");
	   private String value;

	   SmartCardSortProperty(String value) {
	        this.value = value;
	    }

	    public String getValue() {
	        return value;
	    }
}
