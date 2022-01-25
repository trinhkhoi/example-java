package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("org.common")
@EnableJpaRepositories("org.example.repository")
@EntityScan("org.example.entity")
@ComponentScan("org.example.*")
@EnableAsync
@EnableScheduling
@EnableFeignClients
public class ExampleApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(ExampleApplication.class);
		springApplication.addListeners(new ApplicationPidFileWriter());     // register PID write to spring boot. It will write PID to file
		springApplication.run(args);
	}

}
