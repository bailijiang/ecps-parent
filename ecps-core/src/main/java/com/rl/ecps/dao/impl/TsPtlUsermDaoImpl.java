package com.rl.ecps.dao.impl;

import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.TsPtlUserDao;
import com.rl.ecps.model.TsPtlUser;

@Repository
public class TsPtlUsermDaoImpl extends SqlSessionDaoSupport implements TsPtlUserDao {

	String ns = "com.rl.ecps.sqlMap.TsPtlUserMapper.";

	public TsPtlUser selectUserByUserAndPass(Map<String, String> map) {

		return this.getSqlSession().selectOne(ns+"selectUserByUserAndPass", map);
	}

	
	
	

}
