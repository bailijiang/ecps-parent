package com.rl.ecps.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;

@Repository
public class EbSkuDaoImpl extends SqlSessionDaoSupport implements EbSkuDao {

	String ns = "com.rl.ecps.sqlMap.EbSkuMapper.";
	String ns1 = "com.rl.ecps.sqlMap.EbSpecValueMapper.";

	public void saveSku(List<EbSku> skuList, Long itemId) {
		SqlSession sqlSession = this.getSqlSession();
		for(EbSku sku: skuList){
			sku.setItemId(itemId);
			sqlSession.insert(ns+"insert", sku);
			List<EbSpecValue> specValues = sku.getSpecList();
			for(EbSpecValue specValue: specValues){
				specValue.setSkuId(sku.getSkuId());
				sqlSession.insert(ns1+"insert", specValue);
			}
		}
		
	}

	public EbSku getSkuById(Long skuId) {
		return this.getSqlSession().selectOne(ns+"selectByPrimaryKey", skuId);
	}

	public EbSku getSkuDetailById(Long skuId) {
		
		return this.getSqlSession().selectOne(ns+"getSkuDetailById", skuId);
	}

	public int updateStock(Map<String, Object> map) {
		return this.getSqlSession().update(ns+"updateStock", map);
	}

	

	
}
