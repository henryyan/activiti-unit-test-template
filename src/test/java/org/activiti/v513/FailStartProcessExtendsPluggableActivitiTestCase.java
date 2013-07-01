package org.activiti.v513;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.test.Deployment;

/**
 * This class extend PluggableActivitiTestCase
 * 5.12: OK
 * 5.13: Fail
 * 5.14-SNAPSHOT: Fail
 * @author: Henry Yan
 */
public class FailStartProcessExtendsPluggableActivitiTestCase extends PluggableActivitiTestCase {

    @Deployment(resources = "org/activiti/test/v513/oneProcess.bpmn20.xml")
    public void testIsOk() throws Exception {
        runtimeService.startProcessInstanceByKey("oneTaskProcess");
        assertEquals(1, runtimeService.createProcessInstanceQuery().count());
    }

    @Deployment(resources = "org/activiti/test/v513/oneProcess.bpmn20.xml")
    public void testFail1() throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("basicType", "I'm String.");
        runtimeService.startProcessInstanceByKey("oneTaskProcess", vars);
    }

}
