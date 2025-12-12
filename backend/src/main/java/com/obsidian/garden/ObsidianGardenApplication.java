package com.obsidian.garden;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ObsidianGardenApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObsidianGardenApplication.class, args);
	}
}
