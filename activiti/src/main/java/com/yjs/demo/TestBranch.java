package com.yjs.demo;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestBranch {

	//获取流程引擎对象
	//getDefaultProcessEngine方法内部会自动读取名为activiti.cfg.xml文件的配置信息
	ProcessEngine processEngine;

	//初始化方法
	@Before
	public void init(){
		processEngine = ProcessEngines.getDefaultProcessEngine();
		//processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();
	}

	/**部署流程定义
	 * 这一过程产生三张表：
	 * 1.act_re_deployment（部署对象表）：存放流程定义的显示名和部署时间
	 * 2.act_re_procdef（流程定义表）： 存放流程定义的属性信息
	 * 3.act_ge_bytearray（资源文件表）：放流程定义文档
	 */
	@Test
	public void deploymentProcessDefinition(){
		//与流程定义和部署对象相关的Service
		RepositoryService repositoryService = processEngine.getRepositoryService();
		//创建一个部署对象
		DeploymentBuilder deploymentBuilder  = repositoryService.createDeployment();

		deploymentBuilder.name("New TestBranch Deployment");//添加部署的名称
		deploymentBuilder.addClasspathResource("diagrams/TestBranchProcess.bpmn");//从classpath的资源加载，一次只能加载一个文件
		deploymentBuilder.addClasspathResource("diagrams/TestBranchProcess.png");
		try{
			Deployment deployment = deploymentBuilder.deploy();
			//打印我们的流程信息
			System.out.println("流程Id:"+deployment.getId());
			System.out.println("流程Name:"+deployment.getName());
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**启动流程引擎*/
	@Test
	public void startProcessInstance(){
		//获取流程启动Service
		RuntimeService runtimeService = processEngine.getRuntimeService();

		//对应bpmn文件对应的id,act_re_procdef表中对应的KEY_字段)
		String processDefinitionkey="TestBranch";//流程定义的key

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionkey);

		System.out.println("流程实例ID："+processInstance.getId());//流程实例ID
		System.out.println("流程定义ID："+processInstance.getProcessDefinitionId());//流程定义ID
		System.out.println("activitiId::"+processInstance.getActivityId());

	}

	/**查询当前的个人任务(实际就是查询act_ru_task表)*/
	@Test
	public void findMyPersonalTask(){
		String name = "auit";

		//获取事务Service
		TaskService taskService = (TaskService) processEngine.getTaskService();
		List<Task> taskList = taskService.createTaskQuery() ///创建任务查询对象
				.taskAssignee(name) //查询某个人的任务
				.list();
		if(taskList!=null&&taskList.size()>0){
			for(Task task:taskList){
				System.out.println("任务ID："+task.getId());
				System.out.println("任务名称："+task.getName());
				System.out.println("任务的创建时间："+task.getCreateTime());
				System.out.println("任务办理人："+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID："+task.getExecutionId());
				System.out.println("流程定义ID："+task.getProcessDefinitionId());
				System.out.println("#############################################");
			}
		}
	}

	/**完成我的任务*/
	@Test
	public void completeMyPersonalTask(){
		String taskId = "3403"; //任务ID
		TaskService taskService = processEngine.getTaskService();
		Map<String,Object> variables=new HashMap<String,Object>();
		variables.put("message", "auit");
		taskService.complete(taskId,variables); //完成taskId对应的任务
		System.out.println("完成ID为"+taskId+"的任务");
	}


}
