package org.sonatype.mavenbook.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonatype.mavenbook.model.*;
import org.sonatype.mavenbook.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherServiceMock;

    @BeforeEach
    void setUp() throws Exception {
        Location location = new Location();
        location.setCity("Libreville");
        location.setRegion("Estuaire");
        location.setCountry("Gabon");

        Condition condition = new Condition();
        condition.setTemp("80");
        condition.setText("Thunderstorms");

        Atmosphere atmosphere = new Atmosphere();
        atmosphere.setHumidity("84");

        Wind wind = new Wind();
        wind.setChill("78");

        Date weatherDate = Calendar.getInstance().getTime();

        Weather weather = new Weather();
        weather.setLocation(location);
        weather.setCondition(condition);
        weather.setAtmosphere(atmosphere);
        weather.setWind(wind);
        weather.setDate(weatherDate);

        when(weatherServiceMock.retrieveForecast("Libreville")).thenReturn(weather);

    }

    @Test
    void testGetWeatherAtLocationReturnsViewAndModelAndContent() throws Exception {
        mockMvc.perform(get("/weather").param("location", "Libreville"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("weather"))
                .andExpect(content().string(containsString(("Libreville, Estuaire, Gabon"))))
                .andExpect(view().name("weatherPage"))
                .andDo(print());
    }
}