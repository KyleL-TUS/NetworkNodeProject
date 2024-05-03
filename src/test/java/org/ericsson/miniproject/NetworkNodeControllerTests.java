package org.ericsson.miniproject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import javax.print.attribute.standard.Media;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class NetworkNodeControllerTests {

    @Autowired
    TestRestTemplate testRestTemplate;

    @LocalServerPort
    int testServerPort;

    String baseUrl;
    HttpHeaders headers;
    JSONObject jsonObject;
    ObjectMapper objectMapper;

    @BeforeEach
    void init(){
        baseUrl = "http://localhost:"+testServerPort+"/api-v1/nodes";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        jsonObject = new JSONObject();
        objectMapper = new ObjectMapper();
    }

    @Order(1)
    @Test
    void addNode() throws JsonMappingException, JsonProcessingException, JSONException{
        NetworkNode node = new NetworkNode("Test node", "Test loc", 50, 90);
        URI postUri = URI.create(baseUrl);
        jsonObject.put("node", node.toString());
        HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), headers);
        ResponseEntity<String> responseEntityStr = testRestTemplate.postForEntity(postUri, request, String.class);
        JsonNode root = objectMapper.readTree(responseEntityStr.getBody());
        assertNotNull(responseEntityStr.getBody());
        assertNotNull(root.path("node").asText());
    }

}
