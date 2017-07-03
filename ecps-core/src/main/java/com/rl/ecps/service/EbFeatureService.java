package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.EbFeature;

public interface EbFeatureService {
	
	public List<EbFeature> selectCommFeature();
	
	public List<EbFeature> selectSpecFeature();
	
	public List<EbFeature> selectIsSelFeature();
}
