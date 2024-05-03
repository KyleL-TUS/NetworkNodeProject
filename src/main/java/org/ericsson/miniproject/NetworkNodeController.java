package org.ericsson.miniproject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api-v1/nodes")
public class NetworkNodeController {

    private final NetworkNodeService networkNodeService;

    public NetworkNodeController(NetworkNodeService networkNodeService) {
        this.networkNodeService = networkNodeService;
    }

    // REST endpoints for CRUD operations
    @PostMapping
    public ResponseEntity<String>  addNode(@RequestBody NetworkNode node) {
        int nodeId = networkNodeService.addNode(node);
        String responseStr = String.format("msg: %s, nodeId= %d", 
            ((nodeId == -1)? ResponseMsg.NODE_ADDED_FAILURE: ResponseMsg.NODE_ADDED), nodeId);
        
        if(nodeId == -1){
            return ResponseEntity.badRequest().body(responseStr);
        }else{
            
            return ResponseEntity.ok().body(responseStr);
        }
    }

    @GetMapping
    public ResponseEntity<Collection<NetworkNode>> getAllNodes() {
        return ResponseEntity.ok().body(networkNodeService.getAllNodes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NetworkNode> getNodeById(@PathVariable("id") int id) {
        NetworkNode foundNetworkNode = networkNodeService.getNodeById(id);
        if(foundNetworkNode != null){
            return ResponseEntity.ok().body(foundNetworkNode);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateNode(@PathVariable("id") Long id, @RequestBody NetworkNode updatedNode) {

        return ResponseEntity.ok().body(networkNodeService.updateNode(Math.toIntExact(id), updatedNode).toString());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean>  deleteNode(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(networkNodeService.deleteNode(Math.toIntExact(id)));
    }
}
