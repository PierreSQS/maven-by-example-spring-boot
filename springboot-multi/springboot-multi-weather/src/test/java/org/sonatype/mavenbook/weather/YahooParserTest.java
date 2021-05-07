package org.sonatype.mavenbook.weather;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonatype.mavenbook.model.Weather;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(classes = {YahooParser.class})
class YahooParserTest {

    private String content;

    @BeforeEach
    void setUp() throws IOException {
        ResourceLoader resourceLoader = new ClassPathXmlApplicationContext();
        Resource weatherFileResource = resourceLoader.getResource("ny-weather.xml");

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(weatherFileResource.getInputStream()));
        content = bufferedReader.lines().collect(Collectors.joining("\n"));
    }

    @Test
    void testParser() throws Exception {

        log.debug("the Message to parse:{} {}", "\n", content);
        Weather weather = new YahooParser().parseString(content);
        assertEquals("New York", weather.getLocation().getCity());
        assertEquals(" NY", weather.getLocation().getRegion());
        assertEquals("United States", weather.getLocation().getCountry());
        assertEquals("58", weather.getCondition().getTemp());
        assertEquals("Sunny", weather.getCondition().getText());
        assertEquals("55", weather.getWind().getChill());
        assertEquals("44", weather.getAtmosphere().getHumidity());
    }
}