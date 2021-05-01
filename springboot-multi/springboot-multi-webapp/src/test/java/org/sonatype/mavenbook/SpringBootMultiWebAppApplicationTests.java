package org.sonatype.mavenbook;

import org.junit.jupiter.api.Test;
import org.sonatype.mavenbook.controllers.WeatherController;
import org.sonatype.mavenbook.repository.WeatherRepository;
import org.sonatype.mavenbook.weather.WeatherConfig;
import org.sonatype.mavenbook.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringBootMultiWebAppApplicationTests {

	@Test
	void contextLoads(ApplicationContext context) {
		assertThat(context.containsBean("weatherService")).isTrue();
		assertThat(context.containsBean("weatherController")).isTrue();
		assertThat(context.containsBean("historyController")).isTrue();
		assertThat(context.containsBean("weatherConfig")).isTrue();
	}

}
