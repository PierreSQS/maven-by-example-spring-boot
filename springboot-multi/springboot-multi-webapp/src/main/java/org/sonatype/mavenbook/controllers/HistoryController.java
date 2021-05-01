package org.sonatype.mavenbook.controllers;

import org.sonatype.mavenbook.model.Location;
import org.sonatype.mavenbook.weather.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/history")
public class HistoryController {

    private static final String UNKNOWNLOCATION = "Unknown";
    private final WeatherService weatherService;

    public HistoryController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public String displayWeatherHistory(Model model, @RequestParam String city) throws Exception {
        Location unknown = new Location();
        unknown.setCity(UNKNOWNLOCATION);
        unknown.setCountry(UNKNOWNLOCATION);
        unknown.setRegion(UNKNOWNLOCATION);
        model.addAttribute("location", weatherService.findCity(city).orElse(unknown));
        model.addAttribute("weathers",weatherService.getHistory(city));
        return "weatherHistoryPage";
    }
}
