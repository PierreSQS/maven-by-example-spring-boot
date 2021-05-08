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

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {HistoryController.class})
class HistoryControllerTest {

    private Location location;
    private List<Weather> weathers;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WeatherService weatherService;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setCity("Garoua");
        location.setCountry("Cameroon");
        location.setRegion("NO");
        location.setWoeid("1301518");

        Condition condition1 = new Condition();
        condition1.setTemp("90");
        condition1.setText("Partly Cloudy");

        Atmosphere atmosphere1 = new Atmosphere();
        atmosphere1.setHumidity("45");

        Wind wind1 = new Wind();
        wind1.setChill("70");

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

        mockMvc.perform(get("/history").param("city",location.getCity()))
                .andExpect(status().isOk())
                .andExpect(view().name("weatherHistoryPage"))
                .andExpect(request().attribute("location",location))
                .andExpect(request().attribute("weathers",weathers))
                .andExpect(model().attributeExists("location","weathers"))
                .andExpect(content().string(containsString("<strong>Weather History for: Garoua, NO, Cameroon</strong>")))
                .andDo(print());

    }
}