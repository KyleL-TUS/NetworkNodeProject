package org.ericsson.miniproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NetworkNodeTest {

    private NetworkNode networkNode;
    private static final String NODENAME = "Node1";
    private static final String LOCATION = "Location1";
    private static final int LATITUDE = 100;
    private static final int LONGITUDE = 200;

    @BeforeEach
    void setUp() {
        networkNode = new NetworkNode(NODENAME, LOCATION, LATITUDE, LONGITUDE);
    }

    @Test
    void testNetworkNodeConstructor() {
        assertEquals(NODENAME, networkNode.getNode_name());
        assertEquals(LOCATION, networkNode.getNode_location());
        assertEquals(LATITUDE, networkNode.getLatitude());
        assertEquals(LONGITUDE, networkNode.getLongitude());
    }

    @Test
    void testIdSetterGetter() {
        networkNode.setId(1);
        assertEquals(1, networkNode.getId());
    }

    @Test
    void testNodeNameSetterGetter() {
        networkNode.setNode_name("Node2");
        assertEquals("Node2", networkNode.getNode_name());
    }

    @Test
    void testNodeLocationSetterGetter() {
        networkNode.setNode_location("Location2");
        assertEquals("Location2", networkNode.getNode_location());
    }

    @Test
    void testLatitudeSetterGetter() {
        networkNode.setLatitude(101);
        assertEquals(101, networkNode.getLatitude());
    }

    @Test
    void testLongitudeSetterGetter() {
        networkNode.setLongitude(201);
        assertEquals(201, networkNode.getLongitude());
    }
}
