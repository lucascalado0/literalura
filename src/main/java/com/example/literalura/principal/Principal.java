package com.example.literalura.principal;

import com.example.literalura.model.Autor;
import com.example.literalura.model.DadosLivro;
import com.example.literalura.model.Livro;
import com.example.literalura.model.Results;
import com.example.literalura.repository.AutorRepository;
import com.example.literalura.repository.LivrosRepository;
import com.example.literalura.service.ConsumoAPI;
import com.example.literalura.service.ConverteDados;

import java.util.*;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversorDados = new ConverteDados();
    private static String API_URL = "https://gutendex.com/books/?search=";
    private AutorRepository autorRepositorio;
    private LivrosRepository livrosRepositorio;

    List<Livro> livros;
    List<Autor> autores;


    public void exibeMenu() {
        var opcao = -1;

        while (opcao != 0){
            var menu = """
                    -------------- BEM-VINDO AO LITERALURA --------------
                    
                    Escolha uma opcao:
                    
                    1 - Buscar livro por nome
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em determinado ano
                    5 - Listar livros por idioma
                    
                    0 - Sair
                    
                    """;
            try {
                System.out.println(menu);
                opcao = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e){
                var opcaoInvalido = """
                        \n------------ INSIRA UMA OPCAO VALIDA ------------
                        """;
                System.out.println(opcaoInvalido);
                scanner.nextLine();
            }

            switch (opcao){
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 0:
                    var encerrar = """
                            \n------------ ENCERRANDO APLICACAO ------------
                            """;
                    System.out.println(encerrar);
                    break;
                default:
                    System.out.println("\nOpcao Incorreta, tente novamente!");
                    exibeMenu();
                    break;
            }
        }
    }
        public Principal(LivrosRepository livrosRepositorio, AutorRepository autorRepositorio) {
            this.livrosRepositorio = livrosRepositorio;
            this.autorRepositorio = autorRepositorio;
        }

        private void salvarDados (Livro livro, Autor autor){
            Optional<Livro> livroEncontrado = livrosRepositorio.findByTituloContains(livro.getTitulo());
            if (livroEncontrado.isPresent()) {
                System.out.println("Esse livro ja esta cadastrado");
                System.out.println(livro.toString());
            } else {
                try {
                    livrosRepositorio.save(livro);
                    System.out.println("Livro registrado");
                    System.out.println(livro);
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }

            Optional<Autor> autorLocalizado = autorRepositorio.findByNameContains(autor.getNome());
            if (autorLocalizado.isPresent()) {
                System.out.println("Esse autor ja esta cadastrado");
                System.out.println(autor.toString());
            } else {
                try {
                    autorRepositorio.save(autor);
                    System.out.println("autor registrado");
                    System.out.println(autor);
                } catch (Exception e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
    }

    private void buscarLivro() {
        System.out.println("Informe o livro que deseja: ");
        var nomeDoLivro = scanner.nextLine().toLowerCase();
        var json = consumo.obterDados(API_URL + nomeDoLivro.replace(" ", "%20").trim());
        var dados = conversorDados.obterDados(json, Results.class);

        Livro livro;
        Autor autor;
        if (dados.results().isEmpty()) {
            System.out.println("Livro nao encontrado");
        } else {
            DadosLivro dadosLivro = dados.results().get(0);
            livro = new Livro(dadosLivro);
            autor = new Autor().pegaAutor(dadosLivro);
            salvarDados(livro, autor);
        }
    }
    private void listarAutoresRegistrados(){
        System.out.println("----------- Lista de autores registrados -----------");
        autores = autorRepositorio.findAll();
        autores.stream()
                .sorted(Comparator.comparing(Autor::getNome))
                .forEach(System.out::println);
    }
    private void listarLivrosRegistrados(){
        System.out.println("----------- Lista de livros registrados -----------");
        livros = livrosRepositorio.findAll();
        livros.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        System.out.println("Insira o ano que deseja listar: ");
        Integer ano = Integer.valueOf(scanner.nextLine());
        autores = autorRepositorio
                .findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(ano, ano);
        if (autores.isEmpty()){
            System.out.println("Autores vivos nao encontrados");
        } else {
            autores.stream()
                    .sorted()
                    .forEach(System.out::println);
        }
    }
    private void listarLivrosPorIdioma(){
        System.out.println("----------- Lista de livros -----------");
        System.out.println("""
                    \n\\t---- escolha o idioma ----
                    \ten - inglês
                    \tes - espanhol
                    \tfr - francês
                    \tpt - português
                    """);
        String idioma = scanner.nextLine();
        livros = livrosRepositorio.findByIdiomasContais(idioma);
        if (livros.isEmpty()){
            System.out.println("Nao encontrei livros com esse idioma");
            listarLivrosPorIdioma();
        } else {
            livros.stream()
                    .sorted(Comparator.comparing(Livro::getTitulo))
                    .forEach(System.out::println);
        }
    }
}


