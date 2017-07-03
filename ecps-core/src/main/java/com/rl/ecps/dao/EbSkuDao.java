package com.rl.ecps.dao;

import java.util.List;
import java.util.Map;

import com.rl.ecps.model.EbSku;

public interface EbSkuDao {
	
	public void saveSku(List<EbSku> skuList, Long itemId);
	
	public EbSku getSkuById(Long skuId);
	
	public EbSku getSkuDetailById(Long skuId);
	
	public int updateStock(Map<String, Object> map);
}
