package com.rl.ecps.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbBrand;
import com.rl.ecps.model.EbFeature;
import com.rl.ecps.model.EbItem;
import com.rl.ecps.model.EbItemClob;
import com.rl.ecps.model.EbParaValue;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.model.QueryCondition;
import com.rl.ecps.service.EbBrandService;
import com.rl.ecps.service.EbFeatureService;
import com.rl.ecps.service.EbItemService;
import com.rl.ecps.utils.ECPSUtils;
import com.rl.ecps.utils.Page;

@Controller
@RequestMapping("/item")
public class EbItemController {
	
	@Autowired
	private EbItemService itemService;
	
	@Autowired
	private EbBrandService brandService;
	
	@Autowired
	private EbFeatureService featureService;
	
	@RequestMapping("/listItem.do")
	public String listItem(QueryCondition qc, Model model){
		List<EbBrand> bList = brandService.selectBrand();
		//pageNo 刚开始有可能为 null
		if(qc.getPageNo() == null){
			qc.setPageNo(1);
		}
		Page page = itemService.selectItemByCondition(qc);
		model.addAttribute("page", page);
		model.addAttribute("qc", qc);
		model.addAttribute("bList", bList);
		
		return "item/list";
	}
	
	@RequestMapping("/toAddItem.do")
	public String toAddItem(Model model){
		List<EbBrand> bList = brandService.selectBrand();
		List<EbFeature> commList = featureService.selectCommFeature();
		List<EbFeature> specList = featureService.selectSpecFeature();
		model.addAttribute("bList", bList);
		model.addAttribute("commList", commList);
		model.addAttribute("specList", specList);
		return "item/addItem";
	}
	
	@RequestMapping("/addItem.do")
	public String addItem(EbItem item, EbItemClob itemClob, HttpServletRequest request, Integer divNum){
		item.setItemNo(new SimpleDateFormat("YYYYMMddHHmmssSSS").format(new Date()));
		List<EbFeature> commList = featureService.selectCommFeature();
		//存储 paraValue 的集合
		List<EbParaValue> paraList = new ArrayList<EbParaValue>();
		for(EbFeature feature: commList){
			//获得属性的id也就是普通属性在页面上的name
			Long featureId = feature.getFeatureId();
			//3 多选
			if(feature.getInputType() == 3){
				String [] paraArr = request.getParameterValues(featureId+"");
				if(paraArr != null && paraArr.length > 0){
					String paraVals = "";
					for(String val: paraArr){
						paraVals = paraVals + val + ",";
					}
					paraVals = paraVals.substring(0, paraVals.length()-1);
					
					EbParaValue pv = new EbParaValue();
					pv.setFeatureId(featureId);
					pv.setParaValue(paraVals);
					paraList.add(pv);
				}
			}else{
				String paraVal = request.getParameter(featureId+"");
				if(StringUtils.isNotBlank(paraVal)){
					EbParaValue pv = new EbParaValue();
					pv.setFeatureId(featureId);
					pv.setParaValue(paraVal);
					paraList.add(pv);
				}
			}
		}
		
		List<EbSku> skuList = new ArrayList<EbSku>();
		//为了遍历循环拿到 feature.featureId
		List<EbFeature> specList = featureService.selectSpecFeature();
		
		for(int i = 1; i <= divNum; i++){
			//skuType1   showStatus1   sort1     skuPrice1  marketPrice1  stockInventory1  skuUpperLimit1  sku1  location1
			String skuPrice = request.getParameter("skuPrice"+i);
			String stockInventory = request.getParameter("stockInventory"+i);
			if(StringUtils.isNotBlank(skuPrice) && StringUtils.isNotBlank(stockInventory)){
				String skuType = request.getParameter("skuType"+i);
				String showStatus = request.getParameter("showStatus"+i);
				String sort = request.getParameter("sort"+i);
				String marketPrice = request.getParameter("marketPrice"+i);
				String skuUpperLimit = request.getParameter("skuUpperLimit"+i);
				String sku = request.getParameter("sku"+i);
				String location = request.getParameter("location"+i);
				
				EbSku skuObj = new EbSku();
				skuObj.setSkuPrice(new BigDecimal(skuPrice));
				skuObj.setStockInventory(new Integer(stockInventory));
				if(StringUtils.isNotBlank(skuType) && !StringUtils.equals(skuType, "")){
					skuObj.setSkuType(new Short(skuType));
				}
				if(StringUtils.isNotBlank(showStatus) && !StringUtils.equals(showStatus, "")){
					skuObj.setShowStatus(new Short(showStatus));
				}
				if(StringUtils.isNotBlank(sort) && !StringUtils.equals(sort, "")){
					skuObj.setSkuSort(new Integer(sort));
				}
				if(StringUtils.isNotBlank(marketPrice) && !StringUtils.equals(marketPrice, "")){
					skuObj.setMarketPrice(new BigDecimal(marketPrice));
				}
				if(StringUtils.isNotBlank(skuUpperLimit) && !StringUtils.equals(skuUpperLimit, "")){
					skuObj.setSkuUpperLimit(new Integer(skuUpperLimit));
				}
				
				skuObj.setSku(sku);
				skuObj.setLocation(location);
				
				List<EbSpecValue> specValList = new ArrayList<EbSpecValue>();
				for(EbFeature feature: specList){
					Long featureId = feature.getFeatureId();
					String specVal = request.getParameter(featureId+"specradio"+i);
					EbSpecValue specValue = new EbSpecValue();
					specValue.setFeatureId(featureId);
					specValue.setSpecValue(specVal);
					
					specValList.add(specValue);
				}
				skuObj.setSpecList(specValList);
				skuList.add(skuObj);
			}
			
		}
		itemService.saveItem(item, itemClob, paraList, skuList);
		//auditStatus:1 审核通过
		return "redirect:listItem.do?auditStatus=1&showStatus=1";
	}
	
	@RequestMapping("/listAudit.do")
	public String listAudit(QueryCondition qc, Model model){
		List<EbBrand> bList = brandService.selectBrand();
		//pageNo 刚开始有可能为 null
		if(qc.getPageNo() == null){
			qc.setPageNo(1);
		}
		Page page = itemService.selectItemByCondition(qc);
		model.addAttribute("page", page);
		model.addAttribute("qc", qc);
		model.addAttribute("bList", bList);
		return "item/listAudit";
	}
	
	@RequestMapping("/auditItem.do")
	public String auditItem(Long itemId, Short auditStatus, String notes){
		itemService.auditItem(itemId, auditStatus, notes);
		return "redirect:listAudit.do?auditStatus=0&showStatus=1";
	}
	
	@RequestMapping("/showItem.do")
	public String showItem(Long itemId, Short showStatus){
		itemService.showItem(itemId, showStatus);
		String flag = "1";
		if(showStatus == 1){
			flag = "0";
		}
		//0:上架， 1：下架
		return "redirect:listItem.do?auditStatus=1&showStatus="+flag+"";
	}
	
	@RequestMapping("/publishItem.do")
	public void publishItem(Long itemId, PrintWriter out) throws Exception{
		String password = ECPSUtils.readProp("ws_pass");
		String result = itemService.publishItem(itemId, password);
		
		out.write(result);
	}
	
}
