package org.sonatype.mavenbook.weather;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonatype.mavenbook.model.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = {YahooParser.class})
class YahooParserTest {

    private String content;

    @BeforeEach
    void setUp() {
        InputStream nyData =
                getClass().getClassLoader().getResourceAsStream("ny-weather.xml");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(nyData));
        content = bufferedReader.lines().collect(Collectors.joining("\n"));

    }

    @Test
    void testParser() throws Exception {

        log.debug("the Message to parse:{} {}","\n",content);
        Weather weather = new YahooParser().parseString(content);
        assertEquals( "New York", weather.getLocation().getCity() );
        assertEquals( " NY", weather.getLocation().getRegion() );
        assertEquals( "United States", weather.getLocation().getCountry() );
        assertEquals( "58", weather.getCondition().getTemp() );
        assertEquals( "Sunny", weather.getCondition().getText() );
        assertEquals( "55", weather.getWind().getChill() );
        assertEquals( "44", weather.getAtmosphere().getHumidity() );
    }
}