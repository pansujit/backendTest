package com.pitectplus.rcim.backoffice.common;

import org.joda.time.LocalDateTime;

public class CommonUtils {
	
	
	public static String  currentDateInISOFormat() {
		LocalDateTime dateTime= LocalDateTime.now();

		return (dateTime.toString()+"Z");
	}

}
