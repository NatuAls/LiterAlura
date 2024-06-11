package com.aluracursos.LiterAlura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String title;
    @ManyToOne
    private Author author;
    private String language;
    private Integer downloadCount;

    public Book(){}

    public Book(BookData book) {
        this.title = book.title();
        this.author = new Author(book.authors().get(0));
        this.language = book.languages().get(0);
        this.downloadCount = book.downloadCount();
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author){
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    @Override
    public String toString() {
        return """
                \n---- LIBRO ----
                Titulo: %s
                Autor: %s
                Idioma: %s
                Numero de descargas: %d
                ---------------
                """.formatted(this.title, this.author.getName(), this.language, this.downloadCount);
    }
}