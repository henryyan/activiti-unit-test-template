package org.activiti;

import org.activiti.engine.*;
import org.activiti.engine.test.ActivitiRule;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;


/**
 * abstract test
 *
 * @author henryyan
 */
public abstract class AbstractTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");

    protected ProcessEngine processEngine;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected ManagementService managementService;
    protected FormService formService;

    @BeforeClass
    public static void setUpForClass() throws Exception {
        System.out.println("++++++++ start test ++++++++");
    }

    @AfterClass
    public static void testOverForClass() throws Exception {
        System.out.println("-------- end test --------");
    }

    @Before
    public void setUp() throws Exception {
        processEngine = activitiRule.getProcessEngine();
        repositoryService = activitiRule.getRepositoryService();
        runtimeService = activitiRule.getRuntimeService();
        taskService = activitiRule.getTaskService();
        historyService = activitiRule.getHistoryService();
        identityService = activitiRule.getIdentityService();
        managementService = activitiRule.getManagementService();
        formService = activitiRule.getFormService();
    }

}