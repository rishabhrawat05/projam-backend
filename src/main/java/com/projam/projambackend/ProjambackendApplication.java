package com.projam.projambackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProjambackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjambackendApplication.class, args);
	}

}
