package com.rl.ecps.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rl.ecps.model.EbOrder;
import com.rl.ecps.model.TaskBean;
import com.rl.ecps.service.EbOrderService;

@Controller
@RequestMapping("/order")
public class EbOrderController {
	
	@Autowired
	private EbOrderService orderService;
	
	@RequestMapping("/toIndex.do")
	public String toIndex(){
		return "order/index";
	}
	
	@RequestMapping("/listNoPay.do")
	public String listNoPay(Short isCall, String assignee, Model model){
		List<TaskBean> tbList = orderService.selectNoPaidOrder(isCall, assignee);
		model.addAttribute("tbList", tbList);
		model.addAttribute("isCall", isCall);
		return "order/orderPay/orderPay";
	}
	
	@RequestMapping("/listOrderCall.do")
	public String listOrderCall(String assignee, Model model){
		List<TaskBean> tbList = orderService.selectPaidOrder(assignee);
		model.addAttribute("tbList", tbList);
		return "order/orderCall/orderCall";
	}
	
	@RequestMapping("/selectOrderAndDetail.do")
	public String selectOrderAndDetail(Long orderId, Model model){
		EbOrder order = orderService.selectOrderAndDetailById(orderId);
		model.addAttribute("order", order);
		return "order/orderPay/detail";
	}
	
	@RequestMapping("/call.do")
	public String call(Long orderId, Model model){
		orderService.updateCall(orderId);
		return "redirect:listNoPay.do?isCall=0&assignee=noPaidOrderer";
	}
	
	@RequestMapping("/selectPaidOrderDetail.do")
	public String selectPaidOrderDetail(Long orderId, String taskId, Model model){
		TaskBean tb = orderService.selectTaskBeanByOrderIdAndTaskId(orderId, taskId);
		model.addAttribute("tb", tb);
		return "order/orderCall/detail";
	}
	
	@RequestMapping("/completeTask.do")
	public String completeTask(Long orderId, String taskId, String outcome){
		orderService.completeTask(taskId, outcome, orderId);
		return "redirect:listOrderCall.do?assignee=paidOrderer";
	}
	
}
