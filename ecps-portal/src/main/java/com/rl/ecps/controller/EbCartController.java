package com.rl.ecps.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.compass.core.json.JsonArray;
import org.compass.core.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbCart;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.service.EbSkuService;
import com.rl.ecps.utils.ECPSUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/cart")
public class EbCartController {
	
	@Autowired
	private EbSkuService skuService;
	
	@Autowired
	private EbCartService cartService;
	
	@RequestMapping("/listCart.do")
	public String listCart(HttpServletRequest request, HttpServletResponse response, Model model){
		List<EbCart> cartList = cartService.listCart(request, response);
		Integer itemNum = 0;
		BigDecimal totalPrice = new BigDecimal(0);
		for(EbCart cart: cartList){
			itemNum = itemNum + cart.getQuantity();
			totalPrice = totalPrice.add(cart.getSku().getSkuPrice().multiply(new BigDecimal(cart.getQuantity())));
		}
		model.addAttribute("cartList", cartList);
		model.addAttribute("itemNum", itemNum);
		model.addAttribute("totalPrice", totalPrice);
		
		return "shop/car";
	}
	
	@RequestMapping("/addCart.do")
	public void addCart(Long skuId, Integer quantity, 
			HttpServletRequest request, HttpServletResponse response, PrintWriter out){
		cartService.addCart(request, response, skuId, quantity);
		out.write("success");
	}
	
	@RequestMapping("/validStock.do")
	public void validStock(Long skuId, Integer quantity, PrintWriter out){
		String result = "yes";
		EbSku sku = skuService.getSkuById(skuId);
		if(sku.getStockInventory() < quantity){
			result = "no";
		}
		out.write(result);
	}
	
	@RequestMapping("/validStockCar.do")
	public void validStockCar(Long skuId, Integer quantity, PrintWriter out){
//		String exist = "yes";
		String result = "yes";
		EbSku sku = skuService.getSkuById(skuId);
//		System.out.println("*************sku Inventory: "+sku.getStockInventory());
		if(sku.getStockInventory() < quantity){
			result = "no";
		}
		JSONObject jo = new JSONObject();
		jo.accumulate("result", result);
		jo.accumulate("stock", sku.getStockInventory());
		result = jo.toString();
		
		out.write(result);
	}
	
	@RequestMapping("/initCookie.do")
	public void initCookie(HttpServletRequest request, HttpServletResponse response){
		cartService.initCookie(request, response);
	}
	
	@RequestMapping("/validCookie.do")
	public void validCookie(HttpServletRequest request, HttpServletResponse response, PrintWriter out){
		String result = cartService.validCookie(request, response);
		out.write(result);
	}
	
	@RequestMapping("/reduceNum.do")
	public String reduceNum(HttpServletRequest request, HttpServletResponse response,
			Long skuId){
		cartService.reduceNum(request, response, skuId);
		return "redirect:listCart.do";
	}
	@RequestMapping("/deleteItem.do")
	public String deleteItem(HttpServletRequest request, HttpServletResponse response,Long skuId){
		cartService.deleteItem(request, response, skuId);
		return "redirect:listCart.do";
	}
	
	@RequestMapping("/validCar.do")
	public void validCar(HttpServletRequest request, HttpServletResponse response){
		String result = cartService.validCar(request, response);
		ECPSUtils.printJSON(result, response);
	}
}
