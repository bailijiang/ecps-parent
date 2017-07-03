package com.rl.ecps.ws.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.rl.ecps.dao.EbItemDao;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.utils.ECPSUtils;
import com.rl.ecps.utils.FMutil;
import com.rl.ecps.ws.service.EbWSItemService;

public class EbWSItemServiceImpl implements EbWSItemService {
	@Autowired
	private EbItemDao itemDao;
	
	public String publishItem(Long itemId, String password) throws Exception {
		
		String ws_pass = ECPSUtils.readProp("ws_pass");
		if(StringUtils.equals(password, ws_pass)){
			EbItem item = itemDao.selectItemDetailById(itemId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("item", item); // key:item 与 jsp 一致
			FMutil fm = new FMutil();
			fm.outputFile("productDetail.ftl", item.getItemId()+".html", map);
			return "success";
		}else {
			return "fail";
		}
		
	}

}
