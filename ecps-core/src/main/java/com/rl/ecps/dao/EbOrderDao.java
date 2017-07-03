package com.rl.ecps.dao;

import java.util.Map;

import com.rl.ecps.model.EbOrder;
import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

public interface EbOrderDao {
	
	public void saveOrder(EbOrder order);
	
	public void updateOrder(EbOrder order);
	
	public EbOrder selectOrderById(Long orderId);
	
	public EbOrder selectOrderAndDetailById(Long orderId);
	
}
