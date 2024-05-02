package org.ericsson.miniproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NetworkNodeService implements INetworkNodeService{

    @Autowired
    private NetworkNodeRepository nodeRepository;

    @Override
    public ResponseMsg addNode(NetworkNode node) {
        if(!isValidNetworkNode(node)){
            log.info(String.format("msg: %s, node: %s", ResponseMsg.NODE_INVALID, node));
            return ResponseMsg.NODE_INVALID;
        }
        nodeRepository.save(node);
        log.info(String.format("msg: %s, node: %s", ResponseMsg.NODE_ADDED, node));
        return ResponseMsg.NODE_ADDED;
    }

    @Override
    public NetworkNode getNodeById(int id) {
        Optional<NetworkNode> optionalNode = nodeRepository.findById(id);
        return optionalNode.orElse(null);
    }

    @Override
    public NetworkNode updateNode(int id, NetworkNode updatedNode) {
        if (nodeRepository.existsById(id)) {
            updatedNode.setId(id);
            return nodeRepository.save(updatedNode);
        }
        return null;
    }

    @Override
    public boolean deleteNode(int id) {
        if (nodeRepository.existsById(id)) {
            nodeRepository.deleteById(id);
            return true;
        }
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
}
