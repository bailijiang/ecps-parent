package com.rl.ecps.dao;

import java.util.List;

import com.rl.ecps.model.EbBrand;

public interface EbBrandDao {
	
	public void saveBrand(EbBrand brand);
	
	public EbBrand getBrandById(Long brandId);
	
	public void updateBrand(EbBrand brand);
	
	public void deleteBrand(Long brandId);
	
	public List<EbBrand> selectBrand();
	
	public List<EbBrand> selectBrandByName(String brandName);
}
