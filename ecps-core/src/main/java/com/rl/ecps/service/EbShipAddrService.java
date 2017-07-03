package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.EbShipAddr;

public interface EbShipAddrService {
	
	public List<EbShipAddr> selectAddrByUserId(Long userId);
	
	public EbShipAddr selectShipAddrById(Long shipAddrId);
	
	public void saveOrUpdateAddr(EbShipAddr addr);
	
	public void updateDefault(Long shipAddrId, Long userId);
	
	public void deleteShipAddrById(Long shipAddrId);
}
