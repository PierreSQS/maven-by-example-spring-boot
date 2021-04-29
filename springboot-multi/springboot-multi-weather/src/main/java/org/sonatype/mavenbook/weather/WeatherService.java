package org.sonatype.mavenbook.weather;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.sonatype.mavenbook.model.Location;
import org.sonatype.mavenbook.model.Weather;
import org.sonatype.mavenbook.repository.LocationRepository;
import org.sonatype.mavenbook.repository.WeatherRepository;
import org.springframework.stereotype.Service;

@Service
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
		Optional<Location> existingLocation = locationRepository.findByCity(weather.getLocation().getCity());
		if (existingLocation.isEmpty()) {
			locationRepository.save(weather.getLocation());
		} else {
			weather.setLocation(existingLocation.get());
		}
		return weatherRepository.save(weather);
	}

	public List<Weather> getWeatherByLocation(String location) {
		return weatherRepository.findBylocation(location);
	}
}
