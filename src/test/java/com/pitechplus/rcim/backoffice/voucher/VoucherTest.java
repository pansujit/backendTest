package com.pitechplus.rcim.backoffice.voucher;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.data.RcimTestData;
import com.pitechplus.rcim.backoffice.utils.builders.DtoBuilders;
import com.pitectplus.rcim.backoffice.dto.voucher.VoucherGroupEditDto;
import com.pitectplus.rcim.backoffice.dto.voucher.VoucherGroupViewDto;

public class VoucherTest extends BackendAbstract {
	private VoucherGroupEditDto voucherGroupEditDto;
	@BeforeClass
	public void beforeClassCreateVoucher() {
		
		voucherGroupEditDto= DtoBuilders.createVoucherGroupDto(rcimTestData.getAdminCompanyId().toString());
	}
	@Test
	public void createAVoucherGroupTest() throws JsonGenerationException, JsonMappingException, IOException {
		Object x=voucherService.createBackUser(rcimTestData.getSuperAdminToken(), voucherGroupEditDto).getBody();
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		System.out.println("hhhhf");
		String jsonArray = mapper.writeValueAsString(String.class);
		System.out.println("hhhhg");
		VoucherGroupViewDto asList = mapper.readValue(jsonArray, VoucherGroupViewDto.class);
		System.out.println("hhhhi");
		System.out.println("dddd"+asList);

	}
}
