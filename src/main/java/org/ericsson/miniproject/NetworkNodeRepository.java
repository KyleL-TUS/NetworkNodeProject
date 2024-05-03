package org.ericsson.miniproject;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkNodeRepository extends JpaRepository<NetworkNode, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM NETWORK_NODE WHERE latitude= :latitude AND longitude = :longitude LIMIT 1")
    Optional<NetworkNode> findNodeByPosition(@Param("longitude") int longitude, @Param("latitude") int latitude);
}
