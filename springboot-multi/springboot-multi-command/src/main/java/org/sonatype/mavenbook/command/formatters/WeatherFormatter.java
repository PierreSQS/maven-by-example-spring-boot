package org.sonatype.mavenbook.command.formatters;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.sonatype.mavenbook.model.Location;
import org.sonatype.mavenbook.model.Weather;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.List;

@Slf4j
@Component
public class WeatherFormatter {

    @Qualifier("weather")
    private final InputStreamSource weatherSource;

    @Qualifier("history")
    private final InputStreamSource historySource;

    public WeatherFormatter(InputStreamSource weatherSource, InputStreamSource historySource) {
        this.weatherSource = weatherSource;
        this.historySource = historySource;
    }

    public String formatWeather(Weather weather) throws Exception {
        log.info( "Formatting Weather Data" );

        InputStream inputStream = weatherSource.getInputStream();

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
                new InputStreamReader( historySource.getInputStream());
        VelocityContext context = new VelocityContext();
        context.put("location", location );
        context.put("weathers", weathers );
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "", reader);
        return writer.toString();
    }
}
