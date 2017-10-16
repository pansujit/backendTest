package com.pitectplus.rcim.common;

import java.util.Random;

public class CommonMethods {
	
	public static void splitter(String data) {
		Random random= new Random();
		String[] splittedData= data.split("-");
		splittedData[0]=(random.ints(11111, 99999)).toString();
	}

}
