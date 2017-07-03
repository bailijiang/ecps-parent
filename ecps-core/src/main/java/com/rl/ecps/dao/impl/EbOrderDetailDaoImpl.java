package com.rl.ecps.dao.impl;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbOrderDetailDao;
import com.rl.ecps.model.EbOrderDetail;

@Repository
public class EbOrderDetailDaoImpl extends SqlSessionDaoSupport implements EbOrderDetailDao {

	String ns = "com.rl.ecps.sqlMap.EbOrderDetailMapper.";
	
	public void saveOrderDetail(EbOrderDetail OrderDetail) {
		this.getSqlSession().insert(ns+"insert", OrderDetail);
	}

	

}
