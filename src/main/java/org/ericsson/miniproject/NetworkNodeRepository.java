package org.ericsson.miniproject;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkNodeRepository extends JpaRepository<NetworkNode, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM NETWORK_NODE WHERE latitude= :latitude AND longitude = :longitude")
    Optional<NetworkNode> findNodeByPosition(int longitude, int latitude);
}
