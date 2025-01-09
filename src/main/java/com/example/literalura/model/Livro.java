package com.example.literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String autor;
    private String idiomas;
    private Integer dowloads;

    public Livro () {}

    public Livro (DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();
        this.autor = pegaAutor(dadosLivro).getNome();
        this.idiomas = idiomaMod(dadosLivro.idiomas());
        this.dowloads = dadosLivro.dowloads();
    }

    private Autor pegaAutor(DadosLivro dadosLivro) {
        DadosAutor dadosAutor = dadosLivro.autor().get(0);

        return new Autor(dadosAutor);
    }

    private String idiomaMod(List<String> idiomas) {
        if(idiomas == null || idiomas.isEmpty()) {
            return "desconhecido";
        } else {
            return idiomas.get(0);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getDowloads() {
        return dowloads;
    }

    public void setDowloads(Integer dowloads) {
        this.dowloads = dowloads;
    }

    @Override
    public String toString() {
        return "\n\t------ Dados do Livro ------" +
                "\n\tTitulo: " + titulo +
                "\n\tAutor: " + autor +
                "\n\tIdiomas: " + idiomas +
                "\n\tDownloads: " + dowloads +
                "\n\t---------------------";


    }
}
