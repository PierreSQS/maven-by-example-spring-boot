package org.sonatype.mavenbook.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonatype.mavenbook.model.Weather;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {YahooParser.class})
class YahooParserTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testParser() throws Exception {
        InputStream nyData =
                getClass().getClassLoader().getResourceAsStream("ny-weather.xml");
        Weather weather = new YahooParser().parse(nyData );
        assertEquals( "New York", weather.getLocation().getCity() );
        assertEquals( " NY", weather.getLocation().getRegion() );
        assertEquals( "United States", weather.getLocation().getCountry() );
        assertEquals( "58", weather.getCondition().getTemp() );
        assertEquals( "Sunny", weather.getCondition().getText() );
        assertEquals( "55", weather.getWind().getChill() );
        assertEquals( "44", weather.getAtmosphere().getHumidity() );
    }
}