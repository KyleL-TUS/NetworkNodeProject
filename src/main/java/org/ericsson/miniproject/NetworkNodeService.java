package org.ericsson.miniproject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NetworkNodeService implements INetworkNodeService{

    @Autowired
    private NetworkNodeRepository nodeRepository;

    @Override
    public int addNode(NetworkNode node) {
        if(!isValidNetworkNode(node)){
            log.info(String.format("msg: %s, node: %s", ResponseMsg.NODE_INVALID, node));
            return -1;
        }
        
        Optional<NetworkNode> foundNode = nodeRepository.findNodeByPosition(node.getLongitude(), node.getLatitude());
        if(foundNode.isPresent()){
            log.info(String.format("msg: %s, node: %s", ResponseMsg.NODE_FOUND, node));
            return -1;
        }
        node.setNode_location(getLocationService(node.getLatitude(), node.getLongitude()));
        nodeRepository.save(node);
        nodeRepository.flush();
        log.info(String.format("msg: %s, node: %s", ResponseMsg.NODE_ADDED, node));
        return nodeRepository.findNodeByPosition(node.getLongitude(), node.getLatitude()).get().getId();
    }

    @Override
    public NetworkNode getNodeById(int id) {
        Optional<NetworkNode> optionalNode = nodeRepository.findById(id);

        if(optionalNode.isEmpty()){
            log.info(String.format("msg: %s, node-id: %s", ResponseMsg.NODE_NOT_FOUND, id));
            return null;
        }else {
            log.info(String.format("msg: %s, node-: %s", ResponseMsg.NODE_FOUND, optionalNode.get()));
            return optionalNode.get();
        }
    }

    @Override
    public ResponseMsg updateNode(int id, NetworkNode updatedNode) {
        if (nodeRepository.existsById(id)) {
            updatedNode.setId(id);
            updatedNode.setNode_location(getLocationService(updatedNode.getLatitude(), updatedNode.getLongitude()));
            nodeRepository.save(updatedNode);
            log.info(String.format("msg: %s, node: %s", ResponseMsg.NODE_UPDATED, updatedNode));
            return ResponseMsg.NODE_UPDATED;
        }
        log.info(String.format("msg: %s, node-id: %s", ResponseMsg.NODE_NOT_FOUND, id));
        return ResponseMsg.NODE_NOT_FOUND;
    }

    @Override
    public boolean deleteNode(int id) {
        Optional<NetworkNode> deletedNode = nodeRepository.findById(id);
        if (nodeRepository.existsById(id)) {
            nodeRepository.deleteById(id);
            log.info(String.format("msg: %s, node: %s", ResponseMsg.NODE_DELETED, deletedNode));
            return true;
        }
        log.info(String.format("msg: %s, node: %s", ResponseMsg.NODE_DELETED_FAILURE, deletedNode));
        return false;
    }

    @Override
    public List<NetworkNode> getAllNodes() {
        return nodeRepository.findAll();
    }

    @Override
    public boolean isValidNetworkNode(NetworkNode node) {
        // Check if node is null
        if (node == null) {
            return false;
        }
        // Check node name
        if(node.getNode_name() == null || node.getNode_name().isBlank()){
            return false;
        }
        // Check node location
        if(node.getNode_location() == null || node.getNode_location().isBlank()){
            return false;
        }
        // Latitude ranges from -90 to 90 degrees
        if(node.getLatitude() < -90 || node.getLatitude() > 90){
            return false;
        }
        // Longitude ranges from -180 to 180 degrees
        if(node.getLongitude() < -180 || node.getLongitude() > 180){
            return false;
        }
        // Node is valid
        return true;
    }

    @Autowired
    private RestTemplate restTemplate;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public String getLocationService(double Lat, double Lon){
        String location = "Unknown";

        String apiKey = "AIzaSyBvyi-ZxYA0-x8hmMolKY9f_wjAK8xplhc";
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + Lat + "," + Lon + "&key=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(url);
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode resultsNode = rootNode.get("results");
            System.out.println(resultsNode);
            if (resultsNode != null && resultsNode.isArray() && resultsNode.size() > 0) {
                JsonNode areaComponentNode = resultsNode.get(0).get("address_components");
                if (areaComponentNode != null && areaComponentNode.isArray()) {
                    String area = areaComponentNode.get(2).get("short_name").asText();
                    location = area;
                }
                JsonNode regionComponentNode = resultsNode.get(0).get("address_components");
                if (regionComponentNode != null && regionComponentNode.isArray()) {
                    String region = regionComponentNode.get(3).get("short_name").asText();
                    location += ", " + region;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(location);
        return location;
    }
}
