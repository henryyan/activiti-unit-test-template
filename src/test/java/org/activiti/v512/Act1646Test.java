package org.activiti.v512;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.repository.Model;
import org.activiti.engine.test.Deployment;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

/**
 * Bug: Can't complete task created by signal event when attribute cancelActivity = 'false'
 *
 * @author henryyan
 */
public class Act1646Test extends PluggableActivitiTestCase {

  @Deployment(resources = "org/activiti/test/v512/act1646/ok.bpmn20.xml")
  public void testOk() throws Exception {
    long count = repositoryService.createProcessDefinitionQuery().count();
    assertEquals(1, count);
  }

  @Deployment(resources = "org/activiti/test/v512/act1646/process.bpmn20.xml")
  public void testFail() throws Exception {
    long count = repositoryService.createProcessDefinitionQuery().count();
    assertEquals(1, count);
  }

  public void testUseModel() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode editorNode = objectMapper.createObjectNode();
    editorNode.put("id", "canvas");
    editorNode.put("resourceId", "canvas");
    ObjectNode stencilSetNode = objectMapper.createObjectNode();
    stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
    editorNode.put("stencilset", stencilSetNode);
    Model modelData = repositoryService.newModel();

    ObjectNode modelObjectNode = objectMapper.createObjectNode();
    modelObjectNode.put("name", "process");
    modelObjectNode.put("revision", 1);
    modelObjectNode.put("description", "descriptiondescriptiondescriptiondescription");
    modelData.setMetaInfo(modelObjectNode.toString());
    modelData.setName("process");
    modelData.setKey("process");

    repositoryService.saveModel(modelData);
    repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
  }

}
