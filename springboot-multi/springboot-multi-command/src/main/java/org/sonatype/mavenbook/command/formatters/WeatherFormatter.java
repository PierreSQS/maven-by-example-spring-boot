package org.sonatype.mavenbook.command.formatters;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.sonatype.mavenbook.model.Location;
import org.sonatype.mavenbook.model.Weather;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Slf4j
@Component
public class WeatherFormatter {

    private final ApplicationContext ctx;

    public WeatherFormatter(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public String formatWeather(Weather weather) throws Exception {
        log.info( "Formatting Weather Data" );

        InputStream inputStream = ctx.getResource("templates/velocity/weather.vm").getInputStream();

        Reader reader = new InputStreamReader(inputStream);

        VelocityContext context = new VelocityContext();
        context.put("weather", weather );
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "", reader);
        return writer.toString();
    }

    public String   formatHistory(Location location, List<Weather> weathers)
            throws Exception {
        log.info( "Formatting History Data" );
        Reader reader =
                new InputStreamReader( getClass().getClassLoader().
                        getResourceAsStream("velocity/history.vm"));
        VelocityContext context = new VelocityContext();
        context.put("location", location );
        context.put("weathers", weathers );
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "", reader);
        return writer.toString();
    }
}
