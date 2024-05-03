package org.ericsson.miniproject;

import static org.ericsson.miniproject.ResponseMsg.NODE_UPDATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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
import org.springframework.core.ParameterizedTypeReference;
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
    void getAllNodes() throws JsonProcessingException{
        ParameterizedTypeReference<List<NetworkNode>> responseType = new ParameterizedTypeReference<List<NetworkNode>>() {};
        ResponseEntity<List<NetworkNode>> responseEntity = testRestTemplate.exchange(baseUrl, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().isEmpty());

        // Add single node
        NetworkNode node = new NetworkNode("Test node", "Test loc", 50, 90);
        ObjectMapper objectMapper = new ObjectMapper();
        String nodeJson = objectMapper.writeValueAsString(node);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        testRestTemplate.postForEntity(baseUrl, new HttpEntity<>(nodeJson, headers), String.class);

        // verify new list contains newly added node
        responseEntity = testRestTemplate.exchange(baseUrl, HttpMethod.GET, null, responseType);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(1, responseEntity.getBody().get(0).getId());
    }

    @Order(3)
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

    @Order(4)
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
        assertEquals("Mongun-Tayginskiy Rayon, Tuva Republic", getNodeResponse.getBody().getNode_location());
        assertEquals(50, getNodeResponse.getBody().getLatitude());
        assertEquals(90, getNodeResponse.getBody().getLongitude());
    }

    @Order(5)
    @Test
    void updateNode() throws  JsonProcessingException{
        //Adding test node
        NetworkNode node = new NetworkNode("Test node", "Test loc", 50, 90);
        ResponseEntity<String> addNodeResponse = testRestTemplate.postForEntity(baseUrl, node, String.class);
        assertEquals(HttpStatus.OK, addNodeResponse.getStatusCode());

        //Get node based on id
        String[] splitResponse = addNodeResponse.getBody().split("=");
        String nodeIdStr = splitResponse[1].trim();
        int nodeId = Integer.parseInt(nodeIdStr);

        //Retrieve node
        ResponseEntity<NetworkNode> getNodeResponse = testRestTemplate.getForEntity(baseUrl + "/" + nodeId, NetworkNode.class);
        assertEquals(HttpStatus.OK, getNodeResponse.getStatusCode());

        //Update the node
        NetworkNode retrievedNode = getNodeResponse.getBody();
        retrievedNode.setNode_name("Updated Test node");

        //Send updated node to the server
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<NetworkNode> entity = new HttpEntity<>(retrievedNode, headers);

        ResponseEntity<String> updateResponse = testRestTemplate.exchange(baseUrl + "/" + nodeId, HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        System.out.println(updateResponse.getBody());
        assertEquals("Network node successfully updated.", updateResponse.getBody());

    }
}
