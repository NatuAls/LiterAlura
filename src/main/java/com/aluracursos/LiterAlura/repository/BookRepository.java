package com.aluracursos.LiterAlura.repository;

import com.aluracursos.LiterAlura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainsIgnoreCase(String name);

    @Query("SELECT b FROM Book b WHERE b.language = :language")
    List<Book> findByLanguage(String language);
}