package com.rl.ecps.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.service.EbSkuService;

@Service
public class EbSkuServiceImpl implements EbSkuService {
	
	@Autowired
	private EbSkuDao skuDao;
	
	public EbSku getSkuById(Long skuId) {
		return skuDao.getSkuById(skuId);
	}

	
	
	
	

	
	

}
