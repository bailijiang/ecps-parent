package com.rl.ecps.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbConsoleLogDao;
import com.rl.ecps.dao.EbItemClobDao;
import com.rl.ecps.dao.EbItemDao;
import com.rl.ecps.dao.EbParaValueDao;
import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbConsoleLog;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.QueryCondition;
import com.rl.ecps.service.EbItemService;
import com.rl.ecps.stub.EbWSItemService;
import com.rl.ecps.stub.EbWSItemServiceService;
import com.rl.ecps.utils.FMutil;
import com.rl.ecps.utils.Page;
import com.rl.ecps.ws.service.impl.EbWSItemServiceImpl;

@Service
public class EbItemServiceImpl implements EbItemService {

	@Autowired
	private EbItemDao itemDao;
	@Autowired
	private EbItemClobDao itemClobDao;
	@Autowired
	private EbParaValueDao paraValueDao;
	@Autowired
	private EbSkuDao skuDao;
	
	@Autowired
	private EbConsoleLogDao consoleLogDao;
	
	public Page selectItemByCondition(QueryCondition qc) {
		Integer totalCount = itemDao.selectItemByConditionCount(qc);
		
		Page page = new Page();
		//qc 中 pageNo 是已知的(前台传递)
		page.setPageNo(qc.getPageNo());
		page.setTotalCount(totalCount);
		
		Integer startNum = page.getStartNum();
		Integer endNum = page.getEndNum();
		
		qc.setStarNum(startNum);
		qc.setEndNum(endNum);
		List<EbItem> itemList = itemDao.selectItemByCondition(qc);
		
		page.setList(itemList);
		
		return page;
	}

	public void saveItem(EbItem item, EbItemClob itemClob, List<EbParaValue> paraList, List<EbSku> skuList) {
		itemDao.saveItem(item);
		paraValueDao.saveParaValue(paraList, item.getItemId());
		itemClobDao.saveItemClob(itemClob, item.getItemId());
		skuDao.saveSku(skuList, item.getItemId());
	}

	public void auditItem(Long itemId, Short auditStatus, String notes) {
		EbItem item = new EbItem();
		item.setItemId(itemId);
		item.setAuditStatus(auditStatus);
		item.setCheckTime(new Date());
		item.setCheckerUserId(1l);//有用户的话要从 session 中取用户id
		itemDao.updateItem(item);
		
		EbConsoleLog consoleLog = new EbConsoleLog();
		consoleLog.setEntityId(itemId);
		consoleLog.setEntityName("商品表");
		consoleLog.setNotes(notes);
		consoleLog.setOpTime(new Date());
		if(auditStatus == 1){
			consoleLog.setOpType("审核通过");
		}else {
			consoleLog.setOpType("审核不通过");
		}
		consoleLog.setTableName("EB_ITEM");
		consoleLog.setUserId(1l);
		consoleLogDao.saveConsoleLog(consoleLog);
	}

	public void showItem(Long itemId, Short showStatus) {
		
		EbItem item = new EbItem();
		item.setItemId(itemId);
		item.setShowStatus(showStatus);
		item.setOnSaleTime(new Date());
		item.setUpdateUserId(1l);
		itemDao.updateItem(item);
		
		EbConsoleLog consoleLog = new EbConsoleLog();
		consoleLog.setEntityId(itemId);
		consoleLog.setEntityName("商品表");
		
		consoleLog.setOpTime(new Date());
		if(showStatus == 1){
			consoleLog.setOpType("下架");
		}else {
			consoleLog.setOpType("上架");
		}
		consoleLog.setTableName("EB_ITEM");
		consoleLog.setUserId(1l);
		consoleLogDao.saveConsoleLog(consoleLog);
	}

	public List<EbItem> listItem(String price, Long brandId, String paraStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(price) && !StringUtils.equals(price, "")){
			String [] prices = price.split("-");
			map.put("minPrice", prices[0]);
			map.put("maxPrice", prices[1]);
		}
		map.put("brandId", brandId);
		if(StringUtils.isNotBlank(paraStr) && !StringUtils.equals(paraStr, "")){
			String [] paraList = paraStr.split(",");
			map.put("paraList", paraList);
		}
		return itemDao.listItem(map);
	}

	public EbItem selectItemDetailById(Long itemId) {
		return itemDao.selectItemDetailById(itemId);
	}

	public String publishItem(Long itemId, String password) throws Exception {
		EbWSItemServiceService wsItemServiceService = new EbWSItemServiceService();
		EbWSItemService service = wsItemServiceService.getEbWSItemServicePort();
		
		return service.publishItem(itemId, password);
	}

	

	
	

}
