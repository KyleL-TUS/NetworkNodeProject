package org.ericsson.miniproject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


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
