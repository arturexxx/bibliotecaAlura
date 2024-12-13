package com.desafio2.biblioteca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.desafio2.biblioteca.pincipal.Principal;



@SpringBootApplication
public class BibliotecaApplication implements CommandLineRunner {
	
	@Autowired
    private Principal principal;
	

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.muestraElMenu();
		
	}
}
