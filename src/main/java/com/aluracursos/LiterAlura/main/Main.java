package com.aluracursos.LiterAlura.main;

import com.aluracursos.LiterAlura.model.Author;
import com.aluracursos.LiterAlura.model.Book;
import com.aluracursos.LiterAlura.model.BooksData;
import com.aluracursos.LiterAlura.repository.AuthorRepository;
import com.aluracursos.LiterAlura.repository.BookRepository;
import com.aluracursos.LiterAlura.service.APIConsumption;
import com.aluracursos.LiterAlura.service.DataConverter;

import java.util.List;
import java.util.Scanner;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private APIConsumption apiConsumption = new APIConsumption();
    private DataConverter dataConverter = new DataConverter();
    private final String BASE_URL = "https://gutendex.com/books/";
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        int option = -1;
        while (option != 0) {
            printMenu();
            option = readOption();
            handleOption(option);
        }
    }

    public void printMenu() {
            System.out.println("""
                    -------------------
                    Elija una opción a través de su número:
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idiomas
                    0 - Salir
                    """);
    }

    private int readOption() {
        if (scanner.hasNextInt()) {
            int option = scanner.nextInt();
            scanner.nextLine(); // Limpia el buffer del scanner
            return option;
        } else {
            scanner.nextLine(); // Limpia el buffer del scanner
            return -1;
        }
    }

    private void handleOption(int option) {
        switch (option) {
            case 1 -> searchBookByTitle();
            case 2 -> listAllBooks();
            case 3 -> listAllAuthors();
            case 4 -> listAuthorsAliveInYear();
            case 5 -> listBooksByLanguage();
            case 0 -> System.out.println("Saliendo del programa...");
            default -> System.out.println("Por favor ingrese una opción correcta.");
        }
    }

    public void searchBookByTitle(){
        System.out.println("Ingrese el titulo del libro que desea buscar:");
        String title = scanner.nextLine();

        var book = bookRepository.findByTitleContainsIgnoreCase(title);

        if (book.isEmpty()){
            var response = apiConsumption.getData(BASE_URL + "?search=" + title.replace(" ", "+"));
            var booksData = dataConverter.obtainData(response, BooksData.class);

            if (booksData.results().isEmpty()){
                System.out.println("Libro no encontado.");
            } else {
                var newBook = new Book(booksData.results().get(0));
                System.out.println(newBook);

                var author = authorRepository.findByNameContainsIgnoreCase(newBook.getAuthor().getName());

                if (author.isPresent()){
                    author.get().addBook(newBook);
                } else {
                    authorRepository.save(newBook.getAuthor());
                }
                bookRepository.save(newBook);
            }
        } else {
            System.out.println("No se puede registrar el mismo libro mas de una vez.");
        }
    }

    public void listAllBooks(){
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()){
            System.out.println("No se encontraron libros registrados en la base de datos.");
        } else {
            books.forEach(System.out::println);
        }
    }

    public void listAllAuthors(){
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()){
            System.out.println("No se encontraron autores registrados en la base de datos.");
        } else {
            authors.forEach(System.out::println);
        }
    }

    public void listAuthorsAliveInYear(){
        System.out.println("Ingrese el año:");
        var keyboard = scanner.nextLine();
        try {
            var year = Integer.parseInt(keyboard);
            List<Author> authorsByBirthYear = authorRepository.findByBirthYear(year);
            if (authorsByBirthYear.isEmpty()){
                System.out.println("No se encontraron autores registrados vivos en el año proporcionado.");
            } else {
                authorsByBirthYear.forEach(System.out::println);
            }
        } catch (NumberFormatException e){
            System.out.println("Por favor Ingrese un numero valido.");
        }
    }

    public void listBooksByLanguage(){
        System.out.println("""
                            Ingrese el idioma para buscar los libros:
                            es - español
                            en - ingles
                            """);
        var language = scanner.nextLine();
        if ("es".equalsIgnoreCase(language) || "en".equalsIgnoreCase(language)){
            var booksByLanguage = bookRepository.findByLanguage(language);
            booksByLanguage.forEach(System.out::println);
        } else {
            System.out.println("Idioma no válido. Por favor ingrese 'es' o 'en'.");
        }
    }
}