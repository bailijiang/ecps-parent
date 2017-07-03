package com.rl.ecps.dao.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbOrderDao;
import com.rl.ecps.model.EbOrder;

@Repository
public class EbOrderDaoImpl extends SqlSessionDaoSupport implements EbOrderDao {

	String ns = "com.rl.ecps.sqlMap.EbOrderMapper.";

	public void saveOrder(EbOrder order) {
		this.getSqlSession().insert(ns+"insert", order);
	}

	public void updateOrder(EbOrder order) {
		this.getSqlSession().update(ns+"updateByPrimaryKeySelective", order);
		
	}

	public EbOrder selectOrderById(Long orderId) {
		return this.getSqlSession().selectOne(ns+"selectByPrimaryKey", orderId);
	}

	public EbOrder selectOrderAndDetailById(Long orderId) {
		return this.getSqlSession().selectOne(ns+"selectOrderAndDetailById", orderId);
	}

	
	

}
