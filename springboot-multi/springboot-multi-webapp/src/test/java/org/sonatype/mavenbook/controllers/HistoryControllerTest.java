package org.sonatype.mavenbook.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonatype.mavenbook.model.*;
import org.sonatype.mavenbook.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = {HistoryController.class})
class HistoryControllerTest {

    private Location location;
    private Weather weather;
    private List<Weather> weathers;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WeatherService weatherService;


    @BeforeEach
    void setUp() {
        location = new Location();
        location.setCity("Lomé");
        location.setCountry("Togo");
        location.setRegion("Maritime Region");
        location.setWoeid("1440110");

        Condition condition1 = new Condition();
        condition1.setTemp("80");
        condition1.setText("Thunderstorms");

        Atmosphere atmosphere1 = new Atmosphere();
        atmosphere1.setHumidity("84");

        Wind wind1 = new Wind();
        wind1.setChill("78");

        Condition condition2 = new Condition();
        condition2.setTemp("50");
        condition2.setText("Cloudy");

        Atmosphere atmosphere2 = new Atmosphere();
        atmosphere2.setHumidity("100");

        Wind wind2 = new Wind();
        wind2.setChill("90");

        Calendar cal = Calendar.getInstance();
        Date weatherDate1 = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH,4);
        cal.add(Calendar.HOUR,-4);
        cal.add(Calendar.MINUTE,25);
        Date weatherDate2 = cal.getTime();

        Weather weather1 = new Weather();
        weather1.setLocation(location);
        weather1.setCondition(condition1);
        weather1.setAtmosphere(atmosphere1);
        weather1.setWind(wind1);
        weather1.setDate(weatherDate1);

        Weather weather2 = new Weather();
        weather2.setLocation(location);
        weather2.setCondition(condition2);
        weather2.setAtmosphere(atmosphere2);
        weather2.setWind(wind2);
        weather2.setDate(weatherDate2);

        weathers = Arrays.asList(weather1,weather2);

    }

    @Test
    void testGetHistory() throws Exception {
        when(weatherService.findCity(location.getCity())).thenReturn(Optional.of(location));
        when(weatherService.getHistory(location.getCity())).thenReturn(weathers);
        mockMvc.perform(get("/history").queryParam("city","Lomé"))
                .andExpect(status().isOk())
                .andExpect(view().name("weatherHistoryPage"))
                .andExpect(content().string(containsString("Weather History for: Lomé, Maritime Region, Togo")))
                .andDo(print());

    }
}