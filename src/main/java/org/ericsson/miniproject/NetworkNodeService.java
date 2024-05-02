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
        try{
            nodeRepository.save(node);
        }catch(IllegalArgumentException iae){
            System.out.println(iae.getMessage());
        }
        log.info(ResponseMsg.NODE_CREATED.toString());
        return ResponseMsg.NODE_CREATED;
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
        // TODO Node Validation logic
        return false;
    }
}
