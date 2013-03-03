package org.activiti.v511;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;

public class CallActivityDoNotPersistentInitiatorUnitTest {

  @Rule
  public ActivitiRule activitiRule = new ActivitiRule();

  @Test
  @Deployment(resources = { "org/activiti/test/v511/callactivity/MasterProcess.bpmn", "org/activiti/test/v511/callactivity/payment.bpmn" })
  public void test() {
    RuntimeService runtimeService = activitiRule.getRuntimeService();
    IdentityService identityService = activitiRule.getIdentityService();
    HistoryService historyService = activitiRule.getHistoryService();
    TaskService taskService = activitiRule.getTaskService();

    String authenticatedUserId = "henryyan";
    identityService.setAuthenticatedUserId(authenticatedUserId);
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("MasterProcess");
    assertNotNull(processInstance);

    // check start user
    HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(processInstance.getProcessInstanceId()).singleResult();
    assertEquals(authenticatedUserId, historicProcessInstance.getStartUserId());

    // check activiti initiator variable
    Map<String, Object> variables = runtimeService.getVariables(processInstance.getId());
    assertEquals(authenticatedUserId, variables.get("startUserId"));

    Task task = taskService.createTaskQuery().taskAssignee(authenticatedUserId).singleResult();
    assertNotNull(task);

    // complete task with auth user
    identityService.setAuthenticatedUserId(authenticatedUserId);
    taskService.complete(task.getId());

    // query execution of payment
    Execution execution = runtimeService.createExecutionQuery().processDefinitionKey("payment").singleResult();

    // check start user of execution
    historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
    assertEquals(authenticatedUserId, historicProcessInstance.getStartUserId());

    // check variable of initiator in callactivity
    // this is a bug........
    Map<String, Object> variables2 = runtimeService.getVariables(execution.getId());
    assertEquals(authenticatedUserId, variables2.get("startUserInPayment"));
  }

}
