package com.rl.ecps.dao.impl;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbItemClobDao;
import com.rl.ecps.model.EbItemClob;

@Repository
public class EbItemClobDaoImpl extends SqlSessionDaoSupport implements EbItemClobDao {

	String ns = "com.rl.ecps.sqlMap.EbItemClobMapper.";

	public void saveItemClob(EbItemClob itemClob, Long itemId) {
		SqlSession sqlSession = this.getSqlSession();
		itemClob.setItemId(itemId);
		sqlSession.insert(ns+"insert", itemClob);
		
	}

	
	
	

}
