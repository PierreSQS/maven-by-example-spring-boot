package org.sonatype.mavenbook.repository;

import org.sonatype.mavenbook.model.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
    Optional<Location> findByWoeid(String woeid);
    Optional<Location> findByCity(String city);
}
