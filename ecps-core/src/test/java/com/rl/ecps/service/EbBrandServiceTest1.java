package com.rl.ecps.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rl.ecps.model.EbItem;
import com.rl.ecps.utils.FMutil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class EbBrandServiceTest1 {
	
//	@Autowired
//	private EbBrandService brandService; 
//	@Autowired
//	private EbItemService itemService;
	
	@Autowired
	private FlowService flowService;
	
//	@Test
//	public void testSaveBrand() {
//		EbBrand brand = new EbBrand();
//		brand.setBrandName("核桃");
//		brand.setBrandDesc("补脑");
//		brand.setBrandSort(1);
//		brand.setImgs("http://hetaobunao");
//		brand.setWebsite("http://hetaobunao");
//		
//		brandService.saveBrand(brand);
//	}

//	@Test
//	public void testGeneratePage() throws Exception{
//		EbItem item  = itemService.selectItemDetailById((long) 3063);
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("item", item);
//		FMutil fm = new FMutil();
//		fm.outputFile("productDetail.ftl", item.getItemId()+".html", map);
//	}
	
	@Test
	public void deployFlowTest(){
		flowService.deployFlow();
	}

}
