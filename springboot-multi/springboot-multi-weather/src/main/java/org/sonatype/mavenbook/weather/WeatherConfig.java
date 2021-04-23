package org.sonatype.mavenbook.weather;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherConfig {
    @Bean
    public YahooRetriever createYahooRetriever() {
        return new YahooRetriever();
    }

    @Bean
    public YahooParser createYahooParser() {
        return new YahooParser();
    }

}
