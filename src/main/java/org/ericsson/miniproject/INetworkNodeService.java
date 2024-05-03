package org.ericsson.miniproject;

import java.util.List;

public interface INetworkNodeService {

    List<NetworkNode> getAllNodes();

    NetworkNode getNodeById(int id);

    int addNode(NetworkNode node);

    ResponseMsg updateNode(int id, NetworkNode updatedNode);

    boolean deleteNode(int id);

    boolean isValidNetworkNode(NetworkNode node);
}
