package com.rl.ecps.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.event.PublicInvocationEvent;

import com.rl.ecps.exception.EbStockException;
import com.rl.ecps.model.EbOrder;
import com.rl.ecps.model.EbOrderDetail;
import com.rl.ecps.model.TaskBean;

public interface EbOrderService {
	
	public String saveOrder(EbOrder order, List<EbOrderDetail> detailList, 
			HttpServletRequest request, HttpServletResponse response) throws EbStockException;
	
	public void pay(String processInstanceId, Long orderId);
	
	public List<TaskBean> selectNoPaidOrder(Short isCall, String assignee);

	public List<TaskBean> selectPaidOrder(String assignee);
	
	public EbOrder selectOrderAndDetailById(Long orderId);
	
	public void updateCall(Long orderId);
	
	public TaskBean selectTaskBeanByOrderIdAndTaskId(Long orderId, String taskId);
	
	public void completeTask(String taskId, String outcome, Long orderId);
}
