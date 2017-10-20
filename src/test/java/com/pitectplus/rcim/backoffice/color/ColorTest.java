package com.pitectplus.rcim.backoffice.color;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.UUID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.pitechplus.rcim.BackendAbstract;
import com.pitechplus.rcim.backoffice.dto.color.ColorCreateDto;
import com.pitechplus.rcim.backoffice.dto.color.ColorDto;
import com.pitechplus.rcim.backoffice.dto.color.ColorUpdateDto;
import com.rits.cloning.Cloner;


public class ColorTest extends BackendAbstract {
	
	private ColorCreateDto createColorDto;
	private ColorDto colorDto;
	private ColorUpdateDto colorUpdateDto;
	
	@BeforeClass
	public void beforeTestColor() {
		String newColor="red"+UUID.randomUUID().toString().replace("-", "").substring(18,23);
		String newDescription="new Color"+UUID.randomUUID().toString().replace("-", "").substring(12,15);
		createColorDto=ColorCreateDto.builder().code(newColor).description(newDescription).build();
		String updatedColor="blue"+UUID.randomUUID().toString().replace("-", "").substring(24,28);
		String updated="Update Color"+UUID.randomUUID().toString().replace("-", "").substring(6,9);
		colorUpdateDto=ColorUpdateDto.builder().code(updatedColor).description(updated).build();
	}
	@Test(priority=1)
	public void createAColorTest() {
		colorDto=colorService.postColor(rcimTestData.getAdminToken(),createColorDto).getBody();
		Cloner cloner= new Cloner();
		ColorDto y=cloner.deepClone(colorDto);
		y.setCode(createColorDto.getCode());
		y.setDescription(createColorDto.getDescription());
		assertThat("The color is not as expected",colorDto,is(y));
		
	}
	@Test(priority=2,dependsOnMethods="createAColorTest")
	public void getAllColorTest() {
		ColorDto[] data=colorService.getAllColor(rcimTestData.getAdminToken()).getBody();
		assertThat("List of all color doesnot contains given color",Arrays.asList(data), hasItem(colorDto));
	
	}
	@Test(priority=3,dependsOnMethods="createAColorTest")
	public void getSingleColorTest() throws InterruptedException {
		ColorDto data=colorService.getSingleColor(rcimTestData.getAdminToken(),colorDto.getId()).getBody();
		assertThat("The color is not as expected",data,is(colorDto));
	}
	@Test(priority=4,dependsOnMethods="getSingleColorTest")
	public void updateAColorTest() {
		ColorDto updateColor=colorService.updateSingleColor(rcimTestData.getAdminToken(), colorDto.getId(), colorUpdateDto).getBody();
		Cloner cloner= new Cloner();
		ColorDto y=cloner.deepClone(colorDto);
		y.setCode(updateColor.getCode());
		y.setDescription(updateColor.getDescription());
		assertThat("The color is not update as given information",updateColor,is(y));

	}

}
