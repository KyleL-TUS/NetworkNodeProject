package org.ericsson.miniproject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkNodeRepository extends JpaRepository<NetworkNode, Integer> {
}
