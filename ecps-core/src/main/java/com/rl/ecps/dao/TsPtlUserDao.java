package com.rl.ecps.dao;

import java.util.Map;

import com.rl.ecps.model.TsPtlUser;

public interface TsPtlUserDao {
	
	public TsPtlUser selectUserByUserAndPass(Map<String, String> map);
}
