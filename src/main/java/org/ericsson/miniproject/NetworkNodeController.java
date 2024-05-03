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
    public ResponseEntity<ResponseMsg>  addNode(@RequestBody NetworkNode node) {
        return ResponseEntity.ok().body(networkNodeService.addNode(node));
    }

    @GetMapping
    public ResponseEntity<Collection<NetworkNode>> getAllNodes() {
        // ResponseEntity
        return ResponseEntity.ok().body(networkNodeService.getAllNodes());

    }

    @GetMapping("/{id}")
    public ResponseEntity<NetworkNode> getNodeById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(networkNodeService.getNodeById(Math.toIntExact(id)));
    }



    @PutMapping("/{id}")
    public ResponseEntity<NetworkNode> updateNode(@PathVariable("id") Long id, @RequestBody NetworkNode updatedNode) {

        return ResponseEntity.ok().body(networkNodeService.updateNode(Math.toIntExact(id), updatedNode));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean>  deleteNode(@PathVariable("id") Long id) {

        return ResponseEntity.ok().body(networkNodeService.deleteNode(Math.toIntExact(id)));
    }

}
