package com.rl.ecps.service;

import java.util.List;

import com.rl.ecps.model.TaskBean;

public interface FlowService {
	
	public void deployFlow();
	
	public String startProcess(Long orderId);
	
	public void completeTaskByPId(String processInstanceId, String outcome);
	
	public List<TaskBean> selectTaskBeanByAssignee(String assignee);
	
	public TaskBean selectTaskBeanByTaskId(String taskId);
	
	public void completeTaskByTaskId(String taskId, String outcome);
}
