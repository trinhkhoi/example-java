package org.example.test;

import org.example.ExampleApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("org.common")
@EnableJpaRepositories("org.pist.repository")
@EntityScan("org.pist.entity")
@ComponentScan("org.pist.*")
public class PistApplicationTests {
	@Bean
	public TruncateDatabaseService truncateDatabaseService() {
		return new TruncateDatabaseService();
	}

	public static void main(final String[] args) {
		SpringApplication.run(ExampleApplication.class, args);
	}
}
