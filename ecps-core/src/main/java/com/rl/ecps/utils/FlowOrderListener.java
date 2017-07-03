package com.rl.ecps.utils;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class FlowOrderListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3102111796848701319L;

	public void notify(DelegateTask delegateTask) {
		
		//获取当前task Id
		String key = delegateTask.getTaskDefinitionKey();
		delegateTask.setAssignee(key + "er");
		
	}

}
