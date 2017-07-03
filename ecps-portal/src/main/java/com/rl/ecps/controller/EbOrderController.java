package com.rl.ecps.controller;

import com.rl.ecps.exception.EbStockException;
import com.rl.ecps.model.*;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.service.EbOrderService;
import com.rl.ecps.service.EbShipAddrService;
import com.rl.ecps.service.EbSkuService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
public class EbOrderController {
	
	@Autowired
	private EbSkuService skuService;
	
	@Autowired
	private EbCartService cartService;
	
	@Autowired
	private EbShipAddrService addrService;
	
	@Autowired
	private EbOrderService orderService;
	
	@RequestMapping("/toSubmitOrder.do")
	public String toSubmitOrder(HttpServletRequest request, HttpServletResponse response,
			Model model, HttpSession session){
		//查询购物车
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
		
		//查询收获地址
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		if(user != null){
			List<EbShipAddr> addrList = addrService.selectAddrByUserId(user.getPtlUserId());
			model.addAttribute("addrList", addrList);
		}else {
			return "redirect:/user/toLogin.do";
		}
		
		return "shop/confirmProductCase";
	}
	
	@RequestMapping("/submitOrder.do")
	public String submitOrder(HttpServletRequest request, HttpServletResponse response, Model model, 
			EbOrder order, HttpSession session, String address) throws Exception{
		//接收收货地址
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		if(user != null){
			order.setPtlUserId(user.getPtlUserId());
			order.setUsername(user.getUsername());
		}
		order.setOrderNum(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		if(!StringUtils.equals("add", address)){
			EbShipAddr addr = addrService.selectShipAddrById(new Long(address));
			BeanUtils.copyProperties(order, addr);
		}
		
		//接收购物车商品
		List<EbCart> cartList = cartService.listCart(request, response);
		List<EbOrderDetail> detailList = new ArrayList<EbOrderDetail>();
		for(EbCart cart: cartList){
			EbOrderDetail detail = new EbOrderDetail();
			detail.setItemId(cart.getSku().getItem().getItemId());
			detail.setItemName(cart.getSku().getItem().getItemName());
			detail.setItemNo(cart.getSku().getItem().getItemNo());
			detail.setSkuId(cart.getSkuId());
			String specVal = "";
			List<EbSpecValue> specList = cart.getSku().getSpecList();
			for(EbSpecValue spec: specList){
				specVal = specVal + spec.getSpecValue()+",";
			}
			specVal = specVal.substring(0, specVal.length() - 1);
			detail.setSkuSpec(specVal);
			detail.setQuantity(cart.getQuantity());
			detail.setSkuPrice(cart.getSku().getSkuPrice());
			detail.setMarketPrice(cart.getSku().getMarketPrice());
			detailList.add(detail);
		}
		try {
			String processInstanceId = orderService.saveOrder(order, detailList, request, response);
			model.addAttribute("order", order);
			model.addAttribute("processInstanceId", processInstanceId);
		} catch (Exception e) {
			if(e instanceof EbStockException){
				model.addAttribute("tip", "stock_error");
			}
		}
		
		return "shop/confirmProductCase2";
	}
	
	@RequestMapping("/pay.do")
	public void pay(String processInstanceId, Long orderId, PrintWriter out){
		orderService.pay(processInstanceId, orderId);
		out.write("success");
	}
	
}
