package br.com.fiap.smokesignal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "br.com.fiap.smokesignal")
public class SmokesignalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmokesignalApplication.class, args);
	}

}
