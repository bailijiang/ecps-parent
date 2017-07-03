package com.rl.ecps.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rl.ecps.model.TaskBean;
import com.rl.ecps.service.FlowService;
@Service
public class FlowServiceImpl implements FlowService {
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	public void deployFlow() {
		
		DeploymentBuilder db = repositoryService.createDeployment();
		db.addClasspathResource("com/rl/ecps/diagrams/OrderFlow.bpmn")
		  .addClasspathResource("com/rl/ecps/diagrams/OrderFlow.png");
		db.deploy();
		
	}



	public String startProcess(Long orderId) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("OrderFlow", orderId+"");
		return pi.getId();
	}



	public void completeTaskByPId(String processInstanceId, String outcome) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("outcome", outcome);
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		taskService.complete(task.getId(), map);
		
	}



	public List<TaskBean> selectTaskBeanByAssignee(String assignee) {
		List<Task> taskList = taskService.createTaskQuery()
				.processDefinitionKey("OrderFlow")
				.taskAssignee(assignee)
				.orderByTaskCreateTime()
				.desc()
				.list();
		List<TaskBean> tbList = new ArrayList<TaskBean>();
		ProcessInstanceQuery pq = runtimeService.createProcessInstanceQuery();
		for(Task task: taskList){
			TaskBean tb = new TaskBean();
			tb.setTask(task);
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance pi = pq.processDefinitionKey("OrderFlow")
					.processInstanceId(processInstanceId)
					.singleResult();
			tb.setBusinessKey(pi.getBusinessKey());
			tbList.add(tb);
		}
		return tbList;
	}



	public TaskBean selectTaskBeanByTaskId(String taskId) {
		Task task = taskService.createTaskQuery().processDefinitionKey("OrderFlow").taskId(taskId).singleResult();
		TaskBean tb = new TaskBean();
		tb.setTask(task);
		List<String> outcomes = this.getOutcomes(task);
		tb.setOutcomes(outcomes);
		
		return tb;
	}
	
	//获取 task 的 outcomes
	public List<String> getOutcomes(Task task){
		List<String> outcomes = new ArrayList<String>();
		//获取流程定义对象
		ProcessDefinitionEntity pe = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		
		//获取流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionKey("OrderFlow")
		.processInstanceId(task.getProcessInstanceId()).singleResult();
		
		//获取流程部署对象
		ActivityImpl ai = pe.findActivity(pi.getActivityId());
		
		//获得 task 往外走的线路 outcomes
		List<PvmTransition> ptList = ai.getOutgoingTransitions();
		
		for(PvmTransition pt: ptList){
			String name = (String) pt.getProperty("name");
			outcomes.add(name);
		}
		
		return outcomes;
	}



	public void completeTaskByTaskId(String taskId, String outcome) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("outcome", outcome);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		taskService.complete(task.getId(), map);
		
	}


	
}
