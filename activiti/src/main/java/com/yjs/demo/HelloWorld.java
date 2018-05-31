package com.yjs.demo;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class HelloWorld {

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

        deploymentBuilder.name("New Deployment");//添加部署的名称
        deploymentBuilder.addClasspathResource("diagrams/MyProcess.bpmn");//从classpath的资源加载，一次只能加载一个文件
        deploymentBuilder.addClasspathResource("diagrams/MyProcess.png");

        Deployment deployment = deploymentBuilder.deploy();

        //打印我们的流程信息
        System.out.println("流程Id:"+deployment.getId());
        System.out.println("流程Name:"+deployment.getName());
    }

    /**启动流程引擎*/
    @Test
    public void startProcessInstance(){
        //获取流程启动Service
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //对应bpmn文件对应的id,act_re_procdef表中对应的KEY_字段)
        String processDefinitionkey="helloword";//流程定义的key

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionkey);

        System.out.println("流程实例ID："+processInstance.getId());//流程实例ID
        System.out.println("流程定义ID："+processInstance.getProcessDefinitionId());//流程定义ID
        System.out.println("activitiId::"+processInstance.getActivityId());

    }

    /**查询当前的个人任务(实际就是查询act_ru_task表)*/
    @Test
    public void findMyPersonalTask(){
        String name = "employee";

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
        String taskId = "104"; //任务ID
        TaskService taskService = processEngine.getTaskService();
        taskService.complete(taskId); //完成taskId对应的任务
        System.out.println("完成ID为"+taskId+"的任务");
    }





    /**查看流程定义
     *  id:(key):(version):(随机值)
     *  name:对应流程文件process节点的name属性
     *  key:对应流程文件process节点的id属性
     *  version:发布时自动生成的。如果是第一次发布的流程，version默认从1开始；
     *  如果当前流程引擎中已存在相同的流程，则找到当前key对应的最高版本号，在最高版本号上加1*/
    @Test
    public void queryProcessDefinition() throws Exception{
        //获取仓库服务对象，使用版本的升级排列，查询列表
        List<ProcessDefinition> pdList=processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                //添加查询条件
                //.processDefinitionId(processDefinitionId)
                //.processDefinitionKey(processDefinitionKey)
                //.processDefinitionName(processDefinitionName)
                //排序(可以按照id/key/name/version/Cagetory排序)
                .orderByProcessDefinitionVersion().asc()
                //.count()
                //.listPage(firstResult, maxResults)
                //.singleResult()
                .list();//总的结果集数量
        //便利集合，查看内容
        for (ProcessDefinition pd:pdList) {
            System.out.println("id:"+pd.getId());
            System.out.println("name:"+pd.getName());
            System.out.println("key:"+pd.getKey());
            System.out.println("version:"+pd.getVersion());
            System.out.println("resourceName:"+pd.getDiagramResourceName());
            System.out.println("###########################################");
        }
    }

    /**删除流程*/
    @Test
    public void deleteDeployment(){
        //删除发布信息
        String deploymentId="801";
        //获取仓库服务对象
        RepositoryService repositoryService=processEngine.getRepositoryService();
        //普通删除，如果当前规则下有正在执行的流程，则抛异常
        repositoryService.deleteDeployment(deploymentId);
        //级联删除，会删除和当前规则相关的所有信息，正在执行的信息，也包括历史信息
        //repositoryService.deleteDeployment(deploymentId, true);

    }

    /**查看流程附件(查看流程图片)*/
    @Test
    public void viewImage() throws Exception{
        //从仓库中找需要展示的文件
        String deploymentId="901";
        List<String> names=processEngine.getRepositoryService()
                .getDeploymentResourceNames(deploymentId);
        String imageName=null;
        for(String name:names){
            System.out.println("name:"+name);
            if(name.indexOf(".png")>0){
                imageName=name;
                break;
            }
        }
        System.out.println("imageName:"+imageName);
        if(imageName!=null){
            File f=new File("c:/"+imageName);
            //通过部署ID和文件名称得到文件的输入流
            InputStream in = processEngine.getRepositoryService()
                    .getResourceAsStream(deploymentId, imageName);
            FileUtils.copyInputStreamToFile(in, f);
        }
    }


}
