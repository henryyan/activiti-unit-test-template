package org.activiti.v513;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.AbstractTest;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author: Henry Yan
 */
public class Act1731Test extends AbstractTest {

    /**
     * The commit https://github.com/Activiti/Activiti/commit/136859b6e4100d80c34bfc5c43964c8c8e4362de
     * has fixed but only for runtime task...
     * @throws Exception
     */
    @Test
    public void testAct1731() throws Exception {
        String bpmnClasspath = "org/activiti/test/v513/oneProcess.bpmn20.xml";
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource(bpmnClasspath);
        deploymentBuilder.deploy();

        Map<String, Object> vars = new HashMap<String, Object>();
        List<Date> objs = new ArrayList<Date>();
        objs.add(new Date());
        vars.put("list", objs);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess", vars);

        // fail
        ProcessInstance processInstanceNew = runtimeService.createProcessInstanceQuery().includeProcessVariables()
                .processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        assertNotNull(processInstanceNew.getProcessVariables());

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId())
                .includeProcessVariables().singleResult();
        // fail on 5.13, ok on 5.14-SNAPSHOT
        assertNotNull(task.getProcessVariables());

        // it's ok
        taskService.setVariableLocal(task.getId(), "localVar", Arrays.asList(new Date(), new Date()));
        task = taskService.createTaskQuery().includeProcessVariables().includeTaskLocalVariables().singleResult();
        assertNotNull(task.getProcessVariables());
        assertNotNull(task.getTaskLocalVariables());

        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .includeProcessVariables().singleResult();
        assertNotNull(historicProcessInstance);
        // fail
        assertNotNull(historicProcessInstance.getProcessVariables());
    }

}
