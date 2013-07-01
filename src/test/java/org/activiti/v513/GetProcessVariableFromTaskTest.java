package org.activiti.v513;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

/**
 * User: henryyan
 */
public class GetProcessVariableFromTaskTest extends PluggableActivitiTestCase {

    @Deployment(resources = "org/activiti/test/v513/oneProcess.bpmn20.xml")
    public void testOk() throws Exception {
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        Task task = taskService.createTaskQuery().includeProcessVariables().singleResult();
        assertTrue(task.getProcessVariables().isEmpty());
    }

    @Deployment(resources = "org/activiti/test/v513/oneProcess.bpmn20.xml")
    public void testFail1() throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("basicType", "I'm String.");
        runtimeService.startProcessInstanceByKey("oneTaskProcess", vars);
        Task task = taskService.createTaskQuery().includeProcessVariables().singleResult();
        assertEquals(1, task.getProcessVariables().size());
    }

    @Deployment(resources = "org/activiti/test/v513/oneProcess.bpmn20.xml")
    public void testFail2() throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("basicType", Arrays.asList("one", "two", "three"));
        runtimeService.startProcessInstanceByKey("oneTaskProcess", vars);
        Task task = taskService.createTaskQuery().includeProcessVariables().singleResult();
        assertEquals(1, task.getProcessVariables().size());
    }

}
