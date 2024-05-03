package org.ericsson.miniproject;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

}
