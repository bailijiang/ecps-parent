package com.rl.ecps.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.dao.EbOrderDao;
import com.rl.ecps.dao.EbOrderDetailDao;
import com.rl.ecps.dao.EbSkuDao;
import com.rl.ecps.exception.EbStockException;
import com.rl.ecps.model.EbOrder;
import com.rl.ecps.model.EbOrderDetail;
import com.rl.ecps.model.EbSku;
import com.rl.ecps.model.TaskBean;
import com.rl.ecps.service.EbCartService;
import com.rl.ecps.service.EbOrderService;
import com.rl.ecps.service.FlowService;

@Service
public class EbOrderServiceImpl implements EbOrderService {

	@Autowired
	private EbOrderDao orderDao;
	@Autowired
	private EbOrderDetailDao detailDao;
	@Autowired
	private EbSkuDao skuDao;
	@Autowired
	private EbCartService cartService;
	@Autowired
	private FlowService flowService;

	public String saveOrder(EbOrder order, List<EbOrderDetail> detailList, HttpServletRequest request,
			HttpServletResponse response) throws EbStockException{
		orderDao.saveOrder(order);
		Map<String, Object> map = new HashMap<String, Object>();
		for(EbOrderDetail detail: detailList){
			detail.setOrderId(order.getOrderId());
			detailDao.saveOrderDetail(detail);
			//库存处理
//			EbSku sku = skuDao.getSkuById(detail.getSkuId());
//			sku.setStockInventory(sku.getStockInventory() - detail.getQuantity());
//			skuDao.update(sku);
			map.put("skuId", detail.getSkuId());
			map.put("quantity", detail.getQuantity());
			int flag = skuDao.updateStock(map);
			if(flag == 0){
				throw new EbStockException("库存不足");
			}
			
		}
		String processInstanceId = flowService.startProcess(order.getOrderId());
		cartService.clearCart(request, response);
		return processInstanceId;
	}

	public void pay(String processInstanceId, Long orderId) {
		EbOrder order = new EbOrder();
		order.setOrderId(orderId);
		order.setIsPaid((short)1);
		orderDao.updateOrder(order);
		flowService.completeTaskByPId(processInstanceId, "支付");
	}

	public List<TaskBean> selectNoPaidOrder(Short isCall, String assignee) {
		//用到 flowService
		List<TaskBean> tbList = flowService.selectTaskBeanByAssignee(assignee);
		List<TaskBean> tbList1 = new ArrayList<TaskBean>();
		
		for(TaskBean tb: tbList){
			String orderId = tb.getBusinessKey();
			EbOrder order = orderDao.selectOrderById(new Long(orderId));
			if(order.getIsCall().shortValue() == isCall.shortValue()){
				tb.setOrder(order);
				tbList1.add(tb);
			}
		}
		
		return tbList1;
	}

	public EbOrder selectOrderAndDetailById(Long orderId) {
		return orderDao.selectOrderAndDetailById(orderId);
	}

	public void updateCall(Long orderId) {
		EbOrder order = new EbOrder();
		order.setOrderId(orderId);
		order.setIsCall((short)1);
		orderDao.updateOrder(order);
		
	}

	public List<TaskBean> selectPaidOrder(String assignee) {
		List<TaskBean> tbList = flowService.selectTaskBeanByAssignee(assignee);
		
		for(TaskBean tb: tbList){
			String orderId = tb.getBusinessKey();
			EbOrder order = orderDao.selectOrderById(new Long(orderId));
				tb.setOrder(order);
		}
		
		return tbList;
	}

	public TaskBean selectTaskBeanByOrderIdAndTaskId(Long orderId, String taskId) {
		EbOrder order = orderDao.selectOrderById(orderId);
		TaskBean tb =  flowService.selectTaskBeanByTaskId(taskId);
		tb.setOrder(order);
		return tb;
	}

	public void completeTask(String taskId, String outcome, Long orderId) {
		//更新 order updateTime
		EbOrder order = new EbOrder();
		order.setOrderId(orderId);
		order.setUpdateTime(new Date());
		
		flowService.completeTaskByTaskId(taskId, outcome);
		
	}

	
	

}
