package com.rl.ecps.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbBrand;
import com.rl.ecps.model.EbFeature;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.service.EbBrandService;
import com.rl.ecps.service.EbFeatureService;
import com.rl.ecps.service.EbItemService;
import com.rl.ecps.service.EbSkuService;
import com.rl.ecps.utils.ECPSUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/item")
public class EbItemController {
	@Autowired
	private EbBrandService brandService;
	@Autowired
	private EbFeatureService featureService;
	@Autowired
	private EbItemService itemService;
	@Autowired
	private EbSkuService skuService;
	
	@RequestMapping("/toIndex.do")
	public String toIndex(Model model){
		List<EbBrand> bList = brandService.selectBrand();
		List<EbFeature> fList = featureService.selectIsSelFeature();
		model.addAttribute("bList", bList);
		model.addAttribute("fList", fList);
		
		return "index";
	}
	
	@RequestMapping("/listItem.do")
	public String listItem(String price, Long brandId, String paraStr, Model model){
		List<EbItem> itemList = itemService.listItem(price, brandId, paraStr);
		model.addAttribute("itemList", itemList);
		return "phoneClassification";
	}
	
	@RequestMapping("/productDetail.do")
	public String productDetail(Long itemId, Model model){
		EbItem item = itemService.selectItemDetailById(itemId);
		model.addAttribute("item", item);
		return "productDetail";
	}
	
	@RequestMapping("/getSkuById.do")
	public void getSkuById(Long skuId, HttpServletResponse response){
		EbSku sku = skuService.getSkuById(skuId);
		JSONObject jo = new JSONObject();
		jo.accumulate("sku", sku);
		String result = jo.toString();
		ECPSUtils.printJSON(result, response);
	}
}
