package com.pitechplus.rcim.backoffice.data.enums;

public enum Rule {
	
	BOOKING_CREATION_AFTER_DATE("BOOKING_CREATION_AFTER_DATE"),
	BOOKING_CREATION_BEFORE_DATE("BOOKING_CREATION_AFTER_DATE");
	
	private String getData;
	Rule(String getData) {
		this.getData=getData;	
	}
    public String getValue() {
        return getData;
    }

}
