package com.example.literalura;

import com.example.literalura.principal.Principal;
import com.example.literalura.repository.AutorRepository;
import com.example.literalura.repository.LivrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner{
	@Autowired
	private LivrosRepository livrosRepositorio;

	@Autowired
	private AutorRepository autorRepositorio;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(livrosRepositorio, autorRepositorio);
		principal.exibeMenu();
	}
}
