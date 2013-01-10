package org.activiti;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class DeleteHistroyCascadeAllDetailUnitTest {

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  @Deployment(resources = { "org/activiti/test/Process1.bpmn", "org/activiti/test/Process2.bpmn" })
  public void test() {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    
    Map<String, Object> variables = new HashMap<String, Object>();
    variables.put("var1", "value1");
    variables.put("var2", "value2");
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process1", variables);
    assertNotNull(processInstance);

    variables = new HashMap<String, Object>();
    variables.put("var3", "value3");
    variables.put("var4", "value4");
    ProcessInstance processInstance2 = runtimeService.startProcessInstanceByKey("Process2", variables);
    assertNotNull(processInstance2);
    
    // check variables
    HistoryService historyService = activitiRule.getHistoryService();
    long count = historyService.createHistoricVariableInstanceQuery().count();
    assertEquals(4, count);

    // delete runtime execution
    runtimeService.deleteProcessInstance(processInstance.getId(), "reason 1");
    historyService.deleteHistoricProcessInstance(processInstance.getId());
    
    // recheck variables
    // this is a bug: all variables was deleted after delete a history processinstance
    count = historyService.createHistoricVariableInstanceQuery().count();
    assertEquals(2, count);
    
  }

}
