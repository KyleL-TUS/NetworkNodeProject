package org.ericsson.miniproject;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = { "classpath:clear-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("dev")
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class NetworkNodeControllerTests {

    @Autowired
    TestRestTemplate testRestTemplate;

    @LocalServerPort
    int testServerPort;

    String baseUrl;

    @BeforeEach
    void init(){
        baseUrl = "http://localhost:"+testServerPort+"/api-v1/nodes";
    }

    @Order(1)
    @Test
    void addNode() throws JsonMappingException, JsonProcessingException, JSONException{
        NetworkNode node = new NetworkNode("Test node", "Test loc", 50, 90);

        // Convert NetworkNode object to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String nodeJson = objectMapper.writeValueAsString(node);

        // Set up the HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send the POST request
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(baseUrl, new HttpEntity<>(nodeJson, headers), String.class);

        // Check the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("msg: Network node successfully added., nodeId= 1", responseEntity.getBody());

    }
    @Order(2)
    @Test
    void deleteNode() {
        NetworkNode node = new NetworkNode("Test node", "Test loc", 50, 90);
        ResponseEntity<String> addNodeResponse = testRestTemplate.postForEntity(baseUrl, node, String.class);
        assertEquals(HttpStatus.OK, addNodeResponse.getStatusCode());

        // Extract the node ID from the response of adding a node
        String[] splitResponse = addNodeResponse.getBody().split("=");
        String nodeIdStr = splitResponse[1].trim();
        int nodeId = Integer.parseInt(nodeIdStr);

        // Set up the HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send the DELETE request
        ResponseEntity<Boolean> deleteResponse = testRestTemplate.exchange(baseUrl + "/" + nodeId, HttpMethod.DELETE, new HttpEntity<>(headers), Boolean.class);

        // Check the response
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertTrue(deleteResponse.getBody());
    }

    @Order(3)
    @Test
    void getNodeById() {
        NetworkNode node = new NetworkNode("Test node", "Test loc", 50, 90);
        ResponseEntity<String> addNodeResponse = testRestTemplate.postForEntity(baseUrl, node, String.class);
        assertEquals(HttpStatus.OK, addNodeResponse.getStatusCode());

        // Extract the node ID from the response of adding a node
        String[] splitResponse = addNodeResponse.getBody().split("=");
        String nodeIdStr = splitResponse[1].trim();
        int nodeId = Integer.parseInt(nodeIdStr);

        // Send the GET request to retrieve the added node
        ResponseEntity<NetworkNode> getNodeResponse = testRestTemplate.getForEntity(baseUrl + "/" + nodeId, NetworkNode.class);

        // Check the response
        assertEquals(HttpStatus.OK, getNodeResponse.getStatusCode());
        assertNotNull(getNodeResponse.getBody());
        assertEquals("Test node", getNodeResponse.getBody().getNode_name());
        assertEquals("Test loc", getNodeResponse.getBody().getNode_location());
        assertEquals(50, getNodeResponse.getBody().getLatitude());
        assertEquals(90, getNodeResponse.getBody().getLongitude());
    }
}
