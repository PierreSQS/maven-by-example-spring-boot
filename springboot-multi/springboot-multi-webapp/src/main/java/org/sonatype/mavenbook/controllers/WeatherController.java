package org.sonatype.mavenbook.controllers;

import lombok.extern.slf4j.Slf4j;
import org.sonatype.mavenbook.model.Weather;
import org.sonatype.mavenbook.weather.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String getWeatherAtLocation(@RequestParam String location, Model model) throws Exception {
        Weather weather = weatherService.retrieveForecast(location);
        log.info("######## Weather location: {} #########",weather.getLocation().getCity());
        model.addAttribute("weather", weather);
        weatherService.save(weather);
        return "weatherPage";
    }
}
