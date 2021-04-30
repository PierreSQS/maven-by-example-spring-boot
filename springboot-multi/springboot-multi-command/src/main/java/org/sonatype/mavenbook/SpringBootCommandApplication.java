package org.sonatype.mavenbook;

import lombok.extern.slf4j.Slf4j;
import org.sonatype.mavenbook.command.formatters.WeatherFormatter;
import org.sonatype.mavenbook.model.Location;
import org.sonatype.mavenbook.model.Weather;
import org.sonatype.mavenbook.repository.LocationRepository;
import org.sonatype.mavenbook.weather.WeatherService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@SpringBootApplication
public class SpringBootCommandApplication {

    private final LocationRepository locationRepository;
    private final WeatherService weatherService;
    private final WeatherFormatter weatherFormatter;


    public SpringBootCommandApplication(LocationRepository locationRepository, WeatherService weatherService, WeatherFormatter weatherFormatter) {
        this.locationRepository = locationRepository;
        this.weatherService = weatherService;
        this.weatherFormatter = weatherFormatter;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCommandApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            log.info("Submitted Args: {}", Arrays.stream(args).collect(Collectors.toList()));
            if( args[0].equals("weather")) {
                getWeather(args[1]);
            } else {
                getHistory(args[1]);
            }
        };
    }

    public void getWeather(String location) throws Exception {
        Weather weather = weatherService.retrieveForecast(location);
        weather = weatherService.save(weather);
        log.debug("###### Weather Infos saved!!! ######");
        log.debug("Saved Weather: {} {} {} ", weather.getLocation().getCity(), weather.getDate(), weather.getAtmosphere().getHumidity());
        log.info(weatherFormatter.formatWeather(weather));
    }

    public void getHistory(String location) throws Exception {
        Optional<Location> foundLocation = locationRepository.findByCity(location);
        if (foundLocation.isPresent()){
            log.info("##### found location: {} ###########",foundLocation.get().getCity());
            List<Weather> weathersInLocation = weatherService.getWeatherByLocation(foundLocation.get());
            log.info(weatherFormatter.formatHistory(foundLocation.get(), weathersInLocation));
        } else {
            log.info("######## No History present!!!!#########");
        }
    }

}
