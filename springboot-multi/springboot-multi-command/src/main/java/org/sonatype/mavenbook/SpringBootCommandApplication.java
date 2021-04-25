package org.sonatype.mavenbook;

import lombok.extern.slf4j.Slf4j;
import org.sonatype.mavenbook.command.formatters.WeatherFormatter;
import org.sonatype.mavenbook.model.Location;
import org.sonatype.mavenbook.model.Weather;
import org.sonatype.mavenbook.repository.LocationRepository;
import org.sonatype.mavenbook.repository.WeatherRepository;
import org.sonatype.mavenbook.weather.WeatherService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@SpringBootApplication
public class SpringBootCommandApplication {

    private String zip;
    private String location;

    private final WeatherRepository weatherRepository;
    private final LocationRepository locationRepository;
    private final WeatherService weatherService;

    public SpringBootCommandApplication(WeatherRepository weatherRepository, LocationRepository locationRepository, WeatherService weatherService) {
        this.weatherRepository = weatherRepository;
        this.locationRepository = locationRepository;
        this.weatherService = weatherService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCommandApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> {
            log.info("Submitted Args: {}", Arrays.stream(args).collect(Collectors.toList()));
            if( args[0].equals("weather")) {
                getWeather(args[1], context);
            } else {
                getHistory();
            }


        };
    }

    public void getWeather(String location, ApplicationContext ctx) throws Exception {
        Weather weather = weatherService.retrieveForecast(location);
        weather = weatherRepository.save(weather);
        log.debug("###### Weather Infos saved!!! ######");
        log.debug("Saved Weather: {} {} {} ", weather.getLocation().getCity(), weather.getDate(), weather.getAtmosphere().getHumidity());
        log.info(new WeatherFormatter().formatWeather(weather,ctx));
    }

    public void getHistory() throws Exception {
        Location foundLocation = locationRepository.findByZip(zip);
        List<Weather> weathers = weatherRepository.findBylocation(location);
        System.out.print(
                new WeatherFormatter().formatHistory(foundLocation, weathers));
    }

}
