package com.rl.ecps.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbItemDao;
import com.rl.ecps.dao.EbParaValueDao;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.QueryCondition;

@Repository
public class EbParaValueDaoImpl extends SqlSessionDaoSupport implements EbParaValueDao {

	String ns = "com.rl.ecps.sqlMap.EbParaValueMapper.";

	public void saveParaValue(List<EbParaValue> paraValues, Long itemId) {
		SqlSession sqlSession = this.getSqlSession();
		for(EbParaValue paraValue: paraValues){
			paraValue.setItemId(itemId);
			sqlSession.insert(ns+"insert", paraValue);
		}
		
	}

	
	
	

}
