package com.study.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ContainerApplication {

	public static void main(String[] args) {
		// Carrega o arquivo .env
		Dotenv dotenv = Dotenv.load();

		// Define as variáveis de ambiente
		System.setProperty("POSTGRES_DB_URL", dotenv.get("POSTGRES_DB_URL"));
		System.setProperty("POSTGRES_DB_USERNAME", dotenv.get("POSTGRES_DB_USERNAME"));
		System.setProperty("POSTGRES_DB_PASSWORD", dotenv.get("POSTGRES_DB_PASSWORD"));

		// Inicia a aplicação Spring Boot
		SpringApplication.run(ContainerApplication.class, args);
	}
}