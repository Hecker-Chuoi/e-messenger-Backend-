package com.e_messenger.code;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CodeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(CodeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}