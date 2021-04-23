package org.sonatype.mavenbook.repository;

import org.sonatype.mavenbook.model.Location;
import org.sonatype.mavenbook.model.Weather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {
    List<Weather> findBylocation(Location location);
}
