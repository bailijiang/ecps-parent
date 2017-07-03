package com.rl.ecps.service;

import java.util.List;
import java.util.Map;

import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.QueryCondition;
import com.rl.ecps.utils.Page;

public interface EbItemService {
	
	public Page selectItemByCondition(QueryCondition qc);
	
	public void saveItem(EbItem item, EbItemClob itemClob, List<EbParaValue> paraList, List<EbSku> skuList);
	
	public void auditItem(Long itemId, Short auditStatus, String notes);
	
	public void showItem(Long itemId, Short showStatus);
	
	/**
	 * price: 100-4999
	 * brandId: 1003
	 * paraStr: Android4.0,直板,5寸
	 */
	public List<EbItem> listItem(String price, Long brandId, String paraStr);
	
	public EbItem selectItemDetailById(Long itemId);
	
	public String publishItem(Long itemId, String password) throws Exception;
}
