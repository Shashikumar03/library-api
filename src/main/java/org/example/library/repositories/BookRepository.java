package org.example.library.repositories;

import org.example.library.dto.BookDto;
import org.example.library.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {

    boolean existsByBookId(Integer id);


    List<Book> findByBookNameContainingIgnoreCase(String bookName);
    List<Book> findByBookAuthorContainingIgnoreCase(String authorName);
    List<Book> findByBookNameContainingIgnoreCaseOrBookAuthorContainingIgnoreCase(String bookName, String bookAuthor);
    List<Book> findByBookNameContainingOrBookAuthorContaining(String bookName, String bookAuthor);



}
