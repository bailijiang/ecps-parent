package com.rl.ecps.dao.impl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbShipAddrDao;
import com.rl.ecps.model.EbShipAddr;

@Repository
public class EbShipAddrDaoImpl extends SqlSessionDaoSupport implements EbShipAddrDao {

	String ns = "com.rl.ecps.sqlMap.EbShipAddrMapper.";

	public List<EbShipAddr> selectAddrByUserId(Long userId) {
		return this.getSqlSession().selectList(ns+"selectAddrByUserId", userId);
	}

	public EbShipAddr selectShipAddrById(Long shipAddrId) {
		return this.getSqlSession().selectOne(ns+"selectByPrimaryKey", shipAddrId);
	}

	public void saveAddr(EbShipAddr addr) {
		this.getSqlSession().insert(ns+"insert", addr);
	}

	public void updateAddr(EbShipAddr addr) {
		this.getSqlSession().update(ns+"updateByPrimaryKeySelective", addr);
	}

	public void destoryDefault(Long userId) {
		this.getSqlSession().update(ns+"destoryDefault", userId);
	}

	public void deleteShipAddrById(Long shipAddrId) {
		this.getSqlSession().delete(ns+"deleteByPrimaryKey", shipAddrId);
		
	}

	
	
	

}
