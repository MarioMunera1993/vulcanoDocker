package com.grupo1.cursosvulcano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing
public class CursosvulcanoApplication {

	public static void main(String[] args) {
		// Cargamos el archivo .env
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		
		// Pasamos cada variable del .env al Sistema para que Spring las reconozca
		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});

		SpringApplication.run(CursosvulcanoApplication.class, args);
	}

}
