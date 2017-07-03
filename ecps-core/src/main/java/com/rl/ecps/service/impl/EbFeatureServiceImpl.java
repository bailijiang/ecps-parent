package com.rl.ecps.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbFeatureDao;
import com.rl.ecps.model.EbFeature;
import com.rl.ecps.service.EbFeatureService;

@Service
public class EbFeatureServiceImpl implements EbFeatureService {

	
	@Autowired
	private EbFeatureDao featureDao;

	public List<EbFeature> selectCommFeature() {
		return featureDao.selectCommFeature();
	}

	public List<EbFeature> selectSpecFeature() {
		return featureDao.selectSpecFeature();
	}

	public List<EbFeature> selectIsSelFeature() {
		return featureDao.selectIsSelFeature();
	}

	
	

	

}
