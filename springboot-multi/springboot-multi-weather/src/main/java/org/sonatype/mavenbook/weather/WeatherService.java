package org.sonatype.mavenbook.weather;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.sonatype.mavenbook.model.Location;
import org.sonatype.mavenbook.model.Weather;
import org.sonatype.mavenbook.repository.LocationRepository;
import org.sonatype.mavenbook.repository.WeatherRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeatherService {

	private final YahooRetriever yahooRetriever;
	private final YahooParser yahooParser;
	private final LocationRepository locationRepository;
	private final WeatherRepository weatherRepository;

	public WeatherService(YahooRetriever yahooRetriever, YahooParser yahooParser, LocationRepository locationRepository, WeatherRepository weatherRepository) {
		this.yahooRetriever = yahooRetriever;
		this.yahooParser = yahooParser;
		this.locationRepository = locationRepository;
		this.weatherRepository = weatherRepository;
	}

	public Weather retrieveForecast(String location) throws Exception {
		// Retrieve Data
		InputStream dataIn = yahooRetriever.retrieve(location);

		// Parse DataS
		return yahooParser.parse(dataIn);
	}

	public Weather save(Weather weather) {
		// Save location only if it doesn't exist!!!
		Optional<Location> existingLocation = getLocation(weather.getLocation().getCity());
		if (existingLocation.isEmpty()) {
			locationRepository.save(weather.getLocation());
		} else {
			weather.setLocation(existingLocation.get());
		}
		return weatherRepository.save(weather);
	}

	public List<Weather> getHistory(String location) throws Exception {
		List<Weather> weathersInLocation = new ArrayList<>();
		Optional<Location> foundLocation = getLocation(location);
		if (foundLocation.isPresent()){
			log.info("##### found location: {} ###########",foundLocation.get().getCity());
			weathersInLocation = getWeatherByLocation(foundLocation.get());
		} else {
			log.info("######## No History present for Location {}!!!!#########",location);
		}
		return weathersInLocation;
	}

	public Optional<Location> getLocation(String location) {
		return locationRepository.findByCity(location);
	}

	public List<Weather> getWeatherByLocation(Location location) {
		return weatherRepository.findBylocationId(location.getId());
	}
}
