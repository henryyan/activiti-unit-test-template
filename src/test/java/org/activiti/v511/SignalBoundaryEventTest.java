package org.activiti.v511;

import java.util.List;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

/**
 * Bug: Can't complete task created by signal event when attribute cancelActivity = 'false'
 * 
 * @author henryyan
 */
public class SignalBoundaryEventTest extends PluggableActivitiTestCase {

  /**
   * cancelActivity='false'
   */
  @Deployment(resources = "org/activiti/test/v511/signalBoundaryEventNoCancelActivity.bpmn")
  public void testMultiInstance() throws Exception {
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("signalBoundaryEventNoCancelActivity");
    assertNotNull(processInstance);

    // Audit Docs
    Task task = taskService.createTaskQuery().taskName("Audit Docs").singleResult();
    assertNotNull(task);
    ExecutionQuery executionQuery = runtimeService.createExecutionQuery().signalEventSubscriptionName("S_HELP");
    Execution exeuction = executionQuery.singleResult();

    // trigger twice
    runtimeService.signalEventReceived("S_HELP", exeuction.getId());
    runtimeService.signalEventReceived("S_HELP", exeuction.getId());

    List<Execution> list2 = runtimeService.createExecutionQuery().list();
    for (Execution execution : list2) {
      ExecutionEntity ee = (ExecutionEntity) execution;
      System.out.println("execution: id=" + ee.getId() + ", pid=" + ee.getProcessInstanceId() + ", activityId=" + ee.getActivityId() + ", active="
              + ee.isActive());
    }

    // user task Help
    assertEquals(2, taskService.createTaskQuery().taskName("Help").count());

    List<Task> list = taskService.createTaskQuery().taskName("Help").list();
    for (Task task2 : list) {
      System.out.println("task: id=" + task2.getId() + ", executionId=" + task2.getExecutionId() + ", tkey=" + task2.getTaskDefinitionKey() + ", name="
              + task2.getName());
    }

  }

  /**
   * cancelActivity='false'
   */
  @Deployment(resources = "org/activiti/test/v511/signalBoundaryEventNoCancelActivity.bpmn")
  public void testReceiveMessageManualNoCancelActivity() throws Exception {
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("signalBoundaryEventNoCancelActivity");
    assertNotNull(processInstance);

    // Audit Docs
    Task task = taskService.createTaskQuery().taskName("Audit Docs").singleResult();
    assertNotNull(task);
    ExecutionQuery executionQuery = runtimeService.createExecutionQuery().signalEventSubscriptionName("S_HELP");
    Execution exeuction = executionQuery.singleResult();
    runtimeService.signalEventReceived("S_HELP", exeuction.getId());

    // complete "Help" task
    task = taskService.createTaskQuery().taskName("Help").singleResult();
    assertNotNull(task);
    taskService.complete(task.getId());

    // query user task "Audit Docs", two task instances
    List<Task> tasks = taskService.createTaskQuery().taskName("Audit Docs").active().list();
    assertEquals(2, tasks.size());

    // two executions registered signal event
    List<Execution> list = executionQuery.list();
    for (Execution execution : list) {
      ExecutionEntity ee = (ExecutionEntity) execution;
      System.out.println("execution: id=" + ee.getId() + ", pid=" + ee.getProcessInstanceId() + ", activityId=" + ee.getActivityId() + ", active="
              + ee.isActive());
    }
    assertEquals(2, executionQuery.count());

    // complete all tasks
    taskService.complete(tasks.get(0).getId()); // bug: can't complete the first task
    taskService.complete(tasks.get(1).getId()); // OK: the second task can be completed
  }

}
