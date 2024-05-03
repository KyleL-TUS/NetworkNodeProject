package org.ericsson.miniproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
