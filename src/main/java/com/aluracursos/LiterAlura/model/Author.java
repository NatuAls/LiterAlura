package com.aluracursos.LiterAlura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "authors")
public class Author {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String name;
    private Integer birthYear;
    private Integer deathYear;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books;

    public Author(){}

    public Author(AuthorData author) {
        this.name = author.name();
        this.birthYear = author.birthYear();
        this.deathYear = author.deathYear();
    }

    public String getName() {
        return name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void addBook(Book book){
        this.books.add(book);
        book.setAuthor(this);
    }

    @Override
    public String toString() {
        return """
                \nAutor: %s
                Fecha de nacimiento: %d
                fecha de fallecimiento: %d
                Libros: %s
                """.formatted(this.name, this.birthYear, this.deathYear, this.books.stream().map(e -> e.getTitle()).collect(Collectors.toList()));
    }
}