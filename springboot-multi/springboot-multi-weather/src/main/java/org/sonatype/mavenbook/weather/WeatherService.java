package org.sonatype.mavenbook.weather;

import java.io.InputStream;

import org.sonatype.mavenbook.model.Weather;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

	private final YahooRetriever yahooRetriever;
	private final YahooParser yahooParser;

	public WeatherService(YahooRetriever yahooRetriever, YahooParser yahooParser) {
		this.yahooRetriever = yahooRetriever;
		this.yahooParser = yahooParser;
	}

	public Weather retrieveForecast(String location) throws Exception {
		// Retrieve Data
		InputStream dataIn = yahooRetriever.retrieve(location);

		// Parse DataS
		return yahooParser.parse(dataIn);
	}
}
