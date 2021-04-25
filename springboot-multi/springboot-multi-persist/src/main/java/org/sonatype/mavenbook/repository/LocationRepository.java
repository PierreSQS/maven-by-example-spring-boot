package org.sonatype.mavenbook.repository;

import org.sonatype.mavenbook.model.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
    Location findByZip(String zip);
}
