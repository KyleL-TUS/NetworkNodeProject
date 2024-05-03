package org.ericsson.miniproject;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(scripts = { "classpath:clear-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("dev")
@SpringBootTest
public class NetworkNodeServiceTests {

    NetworkNodeRepository nodeRepo;

    @Autowired
    NetworkNodeService fixture;

    @Test
    void addNode_nodeAdded() {
        NetworkNode node = new NetworkNode("Test node A", "Test loc", 70, -70);
        int nodeId = fixture.addNode(node);
        assertEquals(1, nodeId);
    }

    @Test
    void addNullNode_nodeNotAdded() {
        NetworkNode node = null;
        int nodeId = fixture.addNode(node);
        assertEquals(-1, nodeId);
    }

    @Test
    void addInvalidNodeName_nodeNotAdded() {
        NetworkNode node = new NetworkNode("", "Test loc", 70, -70);
        int nodeId = fixture.addNode(node);
        assertEquals(-1, nodeId);
    }

    @Test
    void addInvalidNodeLocation_nodeNotAdded() {
        NetworkNode node = new NetworkNode("Test node", "", 70, -70);
        int nodeId = fixture.addNode(node);
        assertEquals(-1, nodeId);
    }

    @Test
    void addInvalidLatitude_nodeNotAdded() {
        NetworkNode node = new NetworkNode("Test node", "Test loc", 95, -70);
        int nodeId = fixture.addNode(node);
        assertEquals(-1, nodeId);
    }

    @Test
    void addInvalidLongitude_nodeNotAdded() {
        NetworkNode node = new NetworkNode("Test node", "Test loc", 70, -200);
        int nodeId = fixture.addNode(node);
        assertEquals(-1, nodeId);
    }

    @Test
    void getNodeById_nodeFound() {
        NetworkNode testNode = new NetworkNode("Test node B", "Test loc", 50, -50);

        int nodeId = fixture.addNode(testNode);
        assertEquals(1, nodeId);

        NetworkNode foundNode = fixture.getNodeById(testNode.getId());
        assertEquals(testNode.getId(), foundNode.getId());
    }

    @Test
    void getNodeById_nodeNotFound() {
        NetworkNode testNode = new NetworkNode("Test node", "Test loc", 70, -70);
        NetworkNode foundNode = fixture.getNodeById(testNode.getId());
        assertEquals(null, foundNode);
    }

    @Test
    void updateNode_existingNode() {
        NetworkNode originalNode = new NetworkNode("Original", "Loc", 50, 50);
        NetworkNode updatedNode = new NetworkNode("Updated", "Loc", 50, 50);

        fixture.addNode(originalNode);
        ResponseMsg msg = fixture.updateNode(1, updatedNode);
        assertEquals(ResponseMsg.NODE_UPDATED, msg); // Updated assertion
    }

    @Test
    void updateNode_nonExistingNode() {
        NetworkNode updatedNode = new NetworkNode("Updated", "Loc", 50, 50);
        updatedNode.setId(1);

        ResponseMsg result = fixture.updateNode(1, updatedNode);
        assertEquals(ResponseMsg.NODE_NOT_FOUND, result); // Updated assertion
    }

    @Test
    void deleteNode_nodeDeleted(){
        NetworkNode node = new NetworkNode("Test node C", "Test loc", 20, -70);
        int nodeId = fixture.addNode(node);
        assertEquals(1, nodeId);
        assertTrue(fixture.deleteNode(node.getId()));
    }
}