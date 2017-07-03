package com.rl.ecps.dao;

import java.util.List;

import com.rl.ecps.model.EbParaValue;

public interface EbParaValueDao {
	
	public void saveParaValue(List<EbParaValue> paraValues, Long itemId);
}
