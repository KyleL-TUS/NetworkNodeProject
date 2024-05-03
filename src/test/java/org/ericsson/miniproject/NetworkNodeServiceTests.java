package org.ericsson.miniproject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class NetworkNodeServiceTests {

    @MockBean
    NetworkNodeRepository nodeRepo;

    @Autowired
    NetworkNodeService fixture;

    @Test
    void addNode_nodeAdded(){
        NetworkNode node = new NetworkNode("Test node", "Test loc", 70, -70);
        when(nodeRepo.save(node)).thenReturn(node);
        ResponseMsg msg = fixture.addNode(node);
        assertEquals(ResponseMsg.NODE_ADDED, msg);
        verify(nodeRepo, atMostOnce()).save(node);
    }

    @Test
    void addNullNode_nodeNotAdded(){
        NetworkNode node = null;
        ResponseMsg msg = fixture.addNode(node);
        assertEquals(ResponseMsg.NODE_INVALID, msg);
        verify(nodeRepo, never()).save(node);
    }

    @Test
    void addInvalidNodeName_nodeNotAdded(){
        NetworkNode node = new NetworkNode("", "Test loc", 70, -70);
        ResponseMsg msg = fixture.addNode(node);
        assertEquals(ResponseMsg.NODE_INVALID, msg);
        verify(nodeRepo, never()).save(node);
    }

    @Test
    void addInvalidNodeLocation_nodeNotAdded(){
        NetworkNode node = new NetworkNode("Test node", "", 70, -70);
        ResponseMsg msg = fixture.addNode(node);
        assertEquals(ResponseMsg.NODE_INVALID, msg);
        verify(nodeRepo, never()).save(node);
    }

    @Test
    void addInvalidLatitude_nodeNotAdded(){
        NetworkNode node = new NetworkNode("Test node", "Test loc", 95, -70);
        ResponseMsg msg = fixture.addNode(node);
        assertEquals(ResponseMsg.NODE_INVALID, msg);
        verify(nodeRepo, never()).save(node);
    }

    @Test
    void addInvalidLongitude_nodeNotAdded(){
        NetworkNode node = new NetworkNode("Test node", "Test loc", 70, -200);
        ResponseMsg msg = fixture.addNode(node);
        assertEquals(ResponseMsg.NODE_INVALID, msg);
        verify(nodeRepo, never()).save(node);
    }

    @Test
    void getNodeById_nodeFound(){
        NetworkNode testNode = new NetworkNode("Test node", "Test loc", 70, -70);

        when(nodeRepo.save(testNode)).thenReturn(testNode);
        ResponseMsg msg = fixture.addNode(testNode);
        assertEquals(ResponseMsg.NODE_ADDED, msg);
        verify(nodeRepo, atMostOnce()).save(testNode);

        when(nodeRepo.findById(testNode.getId())).thenReturn(Optional.of(testNode));
        NetworkNode foundNode = fixture.getNodeById(testNode.getId());
        assertEquals(testNode, foundNode);
        verify(nodeRepo, atLeast(1)).findById(testNode.getId());
    }

    @Test
    void getNodeById_nodeNotFound(){
        NetworkNode testNode = new NetworkNode("Test node", "Test loc", 70, -70);


        NetworkNode foundNode = fixture.getNodeById(testNode.getId());
        assertEquals(null, foundNode);
        verify(nodeRepo, atLeastOnce()).findById(testNode.getId());
    }

@Test
void updateNode_existingNode() {
    NetworkNode originalNode = new NetworkNode("Original", "Loc", 50, 50);
    NetworkNode updatedNode = new NetworkNode("Updated", "Loc", 50, 50);
    originalNode.setId(1);
    updatedNode.setId(1);

    when(nodeRepo.existsById(1)).thenReturn(true);
    when(nodeRepo.save(updatedNode)).thenReturn(updatedNode);

    ResponseMsg msg = fixture.updateNode(1, updatedNode);
    assertEquals(ResponseMsg.NODE_UPDATED, msg); // Updated assertion
    verify(nodeRepo).save(updatedNode);
}

    @Test
    void updateNode_nonExistingNode() {
        NetworkNode updatedNode = new NetworkNode("Updated", "Loc", 50, 50);
        updatedNode.setId(1);

        when(nodeRepo.existsById(1)).thenReturn(false);

        ResponseMsg result = fixture.updateNode(1, updatedNode);
        assertEquals(ResponseMsg.NODE_NOT_FOUND, result); // Updated assertion
        verify(nodeRepo, never()).save(any(NetworkNode.class));
    }

    @Test
    void deleteNode_nodeDeleted(){
        NetworkNode node = new NetworkNode("Test node", "Test loc", 70, -70);
        when(nodeRepo.save(node)).thenReturn(node);
        ResponseMsg msg = fixture.addNode(node);
        assertEquals(ResponseMsg.NODE_ADDED, msg);
        when(nodeRepo.existsById(node.getId())).thenReturn(true);
        assertTrue(fixture.deleteNode(node.getId()));
        verify(nodeRepo, atMostOnce()).deleteById(node.getId());
    }

    @Test
    void deleteNullNode_nodeNotDeleted(){
        NetworkNode node = new NetworkNode("Test node", "Test loc", 70, -70);
        when(nodeRepo.existsById(node.getId())).thenReturn(false);
        assertFalse(fixture.deleteNode(node.getId()));
        verify(nodeRepo, never()).deleteById(node.getId());
    }
}
