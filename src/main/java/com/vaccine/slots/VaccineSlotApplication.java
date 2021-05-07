package com.vaccine.slots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VaccineSlotApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccineSlotApplication.class, args);
	}

}
