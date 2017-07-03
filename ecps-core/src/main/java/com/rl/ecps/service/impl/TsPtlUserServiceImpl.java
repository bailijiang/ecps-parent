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
import com.rl.ecps.dao.TsPtlUserDao;
import com.rl.ecps.model.EbConsoleLog;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.QueryCondition;
import com.rl.ecps.model.TsPtlUser;
import com.rl.ecps.service.EbItemService;
import com.rl.ecps.service.TsPtlUserService;
import com.rl.ecps.utils.Page;

@Service
public class TsPtlUserServiceImpl implements TsPtlUserService {

	@Autowired
	private TsPtlUserDao tsPtlUserDao;

	public TsPtlUser selectUserByUserAndPass(Map<String, String> map) {
		
		return tsPtlUserDao.selectUserByUserAndPass(map);
	}

	
	

}
