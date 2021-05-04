package org.sonatype.mavenbook.command.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;

@Configuration
public class InputStreamConfig {

    @Bean
    @Qualifier("weather")
    public InputStreamSource weatherSource() {
        return  new ClassPathResource("templates/velocity/weather.vm");
    }

    @Bean
    @Qualifier("history")
    public InputStreamSource historySource() {
        return  new ClassPathResource("templates/velocity/history.vm");
    }
}
