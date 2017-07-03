package com.rl.ecps.dao;

import java.util.List;
import java.util.Map;

import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.QueryCondition;

public interface EbItemDao {
	
	public List<EbItem> selectItemByCondition(QueryCondition qc);
	
	public Integer selectItemByConditionCount(QueryCondition qc);
	
	public void saveItem(EbItem item);
	
	public void updateItem(EbItem item);
	
	public List<EbItem> listItem(Map<String, Object> map);
	
	public EbItem selectItemDetailById(Long itemId);
}
