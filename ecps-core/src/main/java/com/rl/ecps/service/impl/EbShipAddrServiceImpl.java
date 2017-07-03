package com.rl.ecps.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbShipAddrDao;
import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.service.EbShipAddrService;

@Service
public class EbShipAddrServiceImpl implements EbShipAddrService {

	@Autowired
	private EbShipAddrDao addrDao;
	
	public List<EbShipAddr> selectAddrByUserId(Long userId) {
		return addrDao.selectAddrByUserId(userId);
	}

	public EbShipAddr selectShipAddrById(Long shipAddrId) {
		return addrDao.selectShipAddrById(shipAddrId);
	}

	public void saveOrUpdateAddr(EbShipAddr addr) {
		if(addr.getDefaultAddr() == 1){
			addrDao.destoryDefault(addr.getPtlUserId());
		}
		
		if(addr.getShipAddrId() == null){
			addrDao.saveAddr(addr);
		}else {
			addrDao.updateAddr(addr);
		}
		
	}

	public void updateDefault(Long shipAddrId, Long userId) {
		addrDao.destoryDefault(userId);
		EbShipAddr addr = new EbShipAddr();
		addr.setShipAddrId(shipAddrId);
		addr.setDefaultAddr((short) 1);
		addrDao.updateAddr(addr);
	}

	public void deleteShipAddrById(Long shipAddrId) {
		addrDao.deleteShipAddrById(shipAddrId);
		
	}

	
	

}
