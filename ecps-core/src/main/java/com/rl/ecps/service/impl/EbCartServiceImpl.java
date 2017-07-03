package com.rl.ecps.service.impl;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.model.EbCart;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.EbSpecValue;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.utils.ECPSUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

@Service
public class EbCartServiceImpl implements EbCartService {

	
	@Autowired
	private EbSkuDao skuDao;

	public void addCart(HttpServletRequest request, HttpServletResponse response, 
			Long skuId, Integer quantity) {
		List<EbCart> carts = new ArrayList<EbCart>();
		Cookie[] cookies = request.getCookies();
		
		JsonConfig jc = new JsonConfig();
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String []{"sku"});
		
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie: cookies){
				String name = cookie.getName();
				String cookieKey = ECPSUtils.readProp("cart_key");
				if (StringUtils.equals(name, cookieKey)) {
					String result = cookie.getValue();
					result = URLDecoder.decode(result);
					JSONArray ja = JSONArray.fromObject(result);
					
					carts = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					boolean isExsits = false;
					for(EbCart cart: carts){
						if(cart.getSkuId().longValue() == skuId.longValue()){
							cart.setQuantity(cart.getQuantity() + quantity);
							isExsits = true;
						}
					}
					if(!isExsits){
						EbCart cartObj = new EbCart();
						cartObj.setSkuId(skuId);
						cartObj.setQuantity(quantity);
						carts.add(cartObj);
					}
				}
			}
		}
		//第一次登录，没有购物车cookie
		if(carts.size() == 0){
			EbCart cartObj = new EbCart();
			cartObj.setSkuId(skuId);
			cartObj.setQuantity(quantity);
			carts.add(cartObj);
		}
		
		JSONArray ja = JSONArray.fromObject(carts, jc);
		String result = ja.toString();
		result = URLEncoder.encode(result);
		Cookie cookie = new Cookie("cart_key", result);
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		response.addCookie(cookie);
		
	}

	public List<EbCart> listCart(HttpServletRequest request, HttpServletResponse response) {
		List<EbCart> carts = new ArrayList<EbCart>();
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie: cookies){
				String name = cookie.getName();
				String cookieKey = ECPSUtils.readProp("cart_key");
				if (StringUtils.equals(name, cookieKey)) {
					String result = cookie.getValue();
					result = URLDecoder.decode(result);
					JSONArray ja = JSONArray.fromObject(result);
					JsonConfig jc = new JsonConfig();
					jc.setRootClass(EbCart.class);
					jc.setExcludes(new String []{"sku"});
					carts = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					for(EbCart cart: carts){
						EbSku sku = skuDao.getSkuDetailById(cart.getSkuId());
						cart.setSku(sku);
					}
				}
			}
		}
		
		
		return carts;
	}
	

	public void addNum(HttpServletRequest request, HttpServletResponse response, Long skuId) {
		
		
	}

	public void reduceNum(HttpServletRequest request, HttpServletResponse response, Long skuId) {
		List<EbCart> carts = new ArrayList<EbCart>();
		Cookie[] cookies = request.getCookies();
		
		JsonConfig jc = new JsonConfig();
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String []{"sku"});
		
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie: cookies){
				String name = cookie.getName();
				String cookieKey = ECPSUtils.readProp("cart_key");
				if (StringUtils.equals(name, cookieKey)) {
					String result = cookie.getValue();
					result = URLDecoder.decode(result);
					JSONArray ja = JSONArray.fromObject(result);
					
					carts = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					for(EbCart cart: carts){
						if(cart.getSkuId().longValue() == skuId.longValue()){
							cart.setQuantity(cart.getQuantity() - 1);
						}
					}
					
				}
			}
		}
		
		JSONArray ja = JSONArray.fromObject(carts, jc);
		String result = ja.toString();
		result = URLEncoder.encode(result);
		Cookie cookie = new Cookie("cart_key", result);
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		response.addCookie(cookie);
		
	}

	public void deleteItem(HttpServletRequest request, HttpServletResponse response, Long skuId) {
		List<EbCart> carts = new ArrayList<EbCart>();
		Cookie[] cookies = request.getCookies();
		
		JsonConfig jc = new JsonConfig();
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String []{"sku"});
		
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie: cookies){
				String name = cookie.getName();
				String cookieKey = ECPSUtils.readProp("cart_key");
				if (StringUtils.equals(name, cookieKey)) {
					String result = cookie.getValue();
					result = URLDecoder.decode(result);
					JSONArray ja = JSONArray.fromObject(result);
					
					carts = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					
					for(int i=0;i < carts.size();i++){
						EbCart cart = carts.get(i);
						if(cart.getSkuId().longValue() == skuId.longValue()){
							carts.remove(cart);
						}
					}
					
				}
			}
		}
		
		JSONArray ja = JSONArray.fromObject(carts, jc);
		String result = ja.toString();
		result = URLEncoder.encode(result);
		Cookie cookie = new Cookie("cart_key", result);
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		response.addCookie(cookie);
		
	}

	public void clearCart(HttpServletRequest request, HttpServletResponse response) {
		List<EbCart> carts = new ArrayList<EbCart>();
		Cookie[] cookies = request.getCookies();
		
		JsonConfig jc = new JsonConfig();
		jc.setRootClass(EbCart.class);
		jc.setExcludes(new String []{"sku"});
		
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie: cookies){
				String name = cookie.getName();
				String cookieKey = ECPSUtils.readProp("cart_key");
				if (StringUtils.equals(name, cookieKey)) {
					String result = cookie.getValue();
					result = URLDecoder.decode(result);
					JSONArray ja = JSONArray.fromObject(result);
					
					carts = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					
					carts.clear();
					
				}
			}
		}
		
		JSONArray ja = JSONArray.fromObject(carts, jc);
		String result = ja.toString();
		result = URLEncoder.encode(result);
		Cookie cookie = new Cookie("cart_key", result);
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		response.addCookie(cookie);
		
	}

	public String validCookie(HttpServletRequest request, HttpServletResponse response) {
		String result = "no";
	
		Cookie cookie = new Cookie("test", "test");
//		cookie.setMaxAge(1000);
		cookie.setPath("/");
		response.addCookie(cookie);
		Cookie [] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cook: cookies){
				String name = cook.getName();
				String value = cook.getValue();
				if("test".equals(name) && "test".equals(value)){
					result = "yes";
				}
			}
		}
		
		return result;
	}

	public void initCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie("test", "test");
//		cookie.setMaxAge(1000);
		cookie.setPath("/");
		response.addCookie(cookie);
		
	}

	public String validCar(HttpServletRequest request, HttpServletResponse response) {
		String result1 = "success";
		List<EbCart> carts = new ArrayList<EbCart>();
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for(Cookie cookie: cookies){
				String name = cookie.getName();
				String cookieKey = ECPSUtils.readProp("cart_key");
				if (StringUtils.equals(name, cookieKey)) {
					String result = cookie.getValue();
					result = URLDecoder.decode(result);
					JSONArray ja = JSONArray.fromObject(result);
					JsonConfig jc = new JsonConfig();
					jc.setRootClass(EbCart.class);
					jc.setExcludes(new String []{"sku"});
					carts = (List<EbCart>) JSONSerializer.toJava(ja, jc);
					for(EbCart cart: carts){
						EbSku sku = skuDao.getSkuDetailById(cart.getSkuId());
						if(sku.getStockInventory().intValue() < cart.getQuantity().intValue()){
							result1 = sku.getItem().getItemName();
							for(EbSpecValue sv: sku.getSpecList()){
								result1 = result1 + " " + sv.getSpecValue();
							}
							result1 = result1 + " 库存不足 " + cart.getQuantity() + "个";
							break;
						}
					}
				}
			}
		}
		
		return result1;
	}



	
	
	

	
	

}
