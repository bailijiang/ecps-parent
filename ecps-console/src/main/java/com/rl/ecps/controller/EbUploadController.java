package com.rl.ecps.controller;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.rl.ecps.model.EbBrand;
import com.rl.ecps.service.EbBrandService;
import com.rl.ecps.utils.ECPSUtils;
import com.rl.ecps.utils.UploadResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/upload")
public class EbUploadController {
	
	@RequestMapping("/uploadPic.do")
	public void uploadPic(HttpServletRequest request, Writer out) throws IOException{
		
		//把 request 转换成复杂 request
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
		
		//获得文件
		Map<String, MultipartFile> map = mr.getFileMap();
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		String fileInputName = it.next();
		MultipartFile mf = map.get(fileInputName);
		
		//获得文件字节数值
		byte [] bs = mf.getBytes();
		
		//生成存储文件名
		String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Random random = new Random();
		for(int i = 0; i < 3; i++){
			fileName = fileName + random.nextInt(10);
		}
		
		//获得文件后缀
		String oriFileName = mf.getOriginalFilename();
		String suffix = oriFileName.substring(oriFileName.lastIndexOf("."));
		
		//获得上传文件的绝对路径
		String realPath = ECPSUtils.readProp("file_path")+"/upload/"+fileName+suffix;
		
		//获得相对路径，存储到数据库中时使用
		String relativePath = "/upload/"+fileName+suffix;
		
		//创建 jersy 客户端
		Client client = Client.create();
		
		//创建 web 资源对象
		WebResource ws = client.resource(realPath);
		
		//上传
		ws.put(bs);
		
		//利用 JSONObject 将 realPath, relativePath 回写到前端 jsp 
		JSONObject jo = new JSONObject();
		jo.accumulate("realPath", realPath);
		jo.accumulate("relativePath", relativePath);
		String result = jo.toString();
		out.write(result);
		
	}
	@RequestMapping("/uploadForFck.do")
	public void uploadForFck(HttpServletRequest request, HttpServletResponse resp) throws IOException{
		
		//把 request 转换成复杂 request
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
		
		//获得文件
		Map<String, MultipartFile> map = mr.getFileMap();
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		String fileInputName = it.next();
		MultipartFile mf = map.get(fileInputName);
		
		//获得文件字节数值
		byte [] bs = mf.getBytes();
		
		//生成存储文件名
		String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		Random random = new Random();
		for(int i = 0; i < 3; i++){
			fileName = fileName + random.nextInt(10);
		}
		
		//获得文件后缀
		String oriFileName = mf.getOriginalFilename();
		String suffix = oriFileName.substring(oriFileName.lastIndexOf("."));
		
		//获得上传文件的绝对路径
		String realPath = ECPSUtils.readProp("file_path")+"/upload/"+fileName+suffix;
		
		//获得相对路径，存储到数据库中时使用
		String relativePath = "/upload/"+fileName+suffix;
		
		//创建 jersy 客户端
		Client client = Client.create();
		
		//创建 web 资源对象
		WebResource ws = client.resource(realPath);
		
		//上传
		ws.put(bs);
		
		UploadResponse ur = new UploadResponse(UploadResponse.EN_OK, realPath);
		resp.getWriter().print(ur);
		
	}
	
	
	
}
