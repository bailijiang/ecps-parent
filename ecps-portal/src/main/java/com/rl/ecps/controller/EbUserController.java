package com.rl.ecps.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbShipAddr;
import com.rl.ecps.model.TsPtlUser;
import com.rl.ecps.service.EbShipAddrService;
import com.rl.ecps.service.TsPtlUserService;
import com.rl.ecps.utils.ECPSUtils;
import com.rl.ecps.utils.MD5;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/user")
public class EbUserController {
	
	@Autowired
	private TsPtlUserService userService;
	@Autowired
	private EbShipAddrService addrService;
	
	@RequestMapping("/toLogin.do")
	public String toLogin(){
		
		return "passport/login";
	}
	
	@RequestMapping("/getImage.do")
	public void getImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
		 System.out.println("#######################生成数字和字母的验证码#######################");  
	        BufferedImage img = new BufferedImage(68, 22,  
	  
	        BufferedImage.TYPE_INT_RGB);  
	  
	        // 得到该图片的绘图对象    
	  
	        Graphics g = img.getGraphics();  
	  
	        Random r = new Random();  
	  
	        Color c = new Color(200, 150, 255);  
	  
	        g.setColor(c);  
	  
	        // 填充整个图片的颜色    
	  
	        g.fillRect(0, 0, 68, 22);  
	  
	        // 向图片中输出数字和字母    
	  
	        StringBuffer sb = new StringBuffer();  
	  
	        char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();  
	  
	        int index, len = ch.length;  
	  
	        for (int i = 0; i < 4; i++) {  
	  
	            index = r.nextInt(len);  
	  
	            g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt  
	  
	            (255)));  
	  
	            g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));  
	            // 输出的  字体和大小                      
	  
	            g.drawString("" + ch[index], (i * 15) + 3, 18);  
	            //写什么数字，在图片 的什么位置画    
	  
	            sb.append(ch[index]);  
	  
	        }  
	  
	        request.getSession().setAttribute("piccode", sb.toString());  
	  
	        ImageIO.write(img, "JPG", response.getOutputStream());  
	}
	
	@RequestMapping("/login.do")
	public String login(String username, String password, String captcha,
			HttpSession session, Model model){
		String picCode = (String) session.getAttribute("piccode");
		if(!StringUtils.equalsIgnoreCase(picCode, captcha)){
			model.addAttribute("tip", "caperror");
			return "passport/login";
		}
		password = new MD5().GetMD5Code(password);
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		TsPtlUser user = userService.selectUserByUserAndPass(map);
		if(user == null){
			model.addAttribute("tip", "userpasserror");
			return "passport/login";
		}
		session.setAttribute("user", user);
		return "redirect:/item/toIndex.do";
	}
	
	@RequestMapping("/getUser.do")
	public void getUser(HttpSession session, HttpServletResponse response){
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		JSONObject jo = new JSONObject();
//		if(user != null){
//			jo.accumulate("user", user);
//		}
		jo.accumulate("user", user);
		
		String result = jo.toString();
		ECPSUtils.printJSON(result, response);
		
	}
	
	@RequestMapping("/login/toPersonal.do")
	public String toPersonal(){
		
		return "person/index";
	}
	
	@RequestMapping("/login/toAddr.do")
	public String toAddr(HttpSession session, Model model){
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		List<EbShipAddr> addrList = addrService.selectAddrByUserId(user.getPtlUserId());
		model.addAttribute("addrList", addrList);
		return "person/deliverAddress";
	}
	
	@RequestMapping("/login/getAddrById.do")
	public void getAddrById(Long shipAddrId, HttpServletResponse response){
		EbShipAddr addr = addrService.selectShipAddrById(shipAddrId);
		JSONObject jo = new JSONObject();
		jo.accumulate("addr", addr);
		String result = jo.toString();
		ECPSUtils.printJSON(result, response);
		
	}
	
	@RequestMapping("/login/saveOrUpdateAddr.do")
	public String saveOrUpdateAddr(HttpSession session, EbShipAddr addr){
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		if(addr.getDefaultAddr() == null){
			addr.setDefaultAddr((short)0);
		}
		addr.setPtlUserId(user.getPtlUserId());
		addrService.saveOrUpdateAddr(addr);
		return "redirect:/user/login/toAddr.do";
	}
	
	@RequestMapping("/login/updateDefault.do")
	public String updateDefault(HttpSession session, Long shipAddrId){
		TsPtlUser user = (TsPtlUser) session.getAttribute("user");
		addrService.updateDefault(shipAddrId, user.getPtlUserId());
		
		return "redirect:/user/login/toAddr.do";
	}
	
//	@RequestMapping("/login/deleteAddrById.do")
//	public String deleteShipAddrById(Long shipAddrId){
//		addrService.deleteShipAddrById(shipAddrId);
//		return "redirect:/user/login/toAddr.do";
//	}
	@RequestMapping("/login/deleteAddrById.do")
	public void deleteShipAddrById(Long shipAddrId, HttpServletResponse response){
		addrService.deleteShipAddrById(shipAddrId);
		try {
			PrintWriter out = response.getWriter();
			out.print("1");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/loginAjax.do")
	public void loginAjax(String username, String password, String captcha, 
			HttpSession session, PrintWriter out){
		String picCode = (String) session.getAttribute("piccode");
		if(!StringUtils.equalsIgnoreCase(picCode, captcha)){
			out.write("caperror");
			return;
		}
		password = new MD5().GetMD5Code(password);
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		TsPtlUser user = userService.selectUserByUserAndPass(map);
		if(user == null){
			out.write("userpasserror");
			return;
		}
		session.setAttribute("user", user);
		out.write("success");
	}

}
