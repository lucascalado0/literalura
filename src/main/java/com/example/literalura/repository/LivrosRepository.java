package com.example.literalura.repository;

import com.example.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivrosRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByTituloContains(String titulo);
    List<Livro> findByIdiomasContais(String idiomas);
}
