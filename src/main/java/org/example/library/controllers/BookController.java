package org.example.library.controllers;

import jakarta.validation.Valid;
import org.example.library.dto.BookDto;
import org.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;



    @PostMapping("/")
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto){

        BookDto book = this.bookService.createBook(bookDto);
        return new ResponseEntity<>(bookDto, HttpStatus.CREATED);
    }

    @GetMapping("/allbooks")
    public  ResponseEntity<List<BookDto>>getAllBooks(){

        List<BookDto> allBooks = this.bookService.getAllBooks();
        return new ResponseEntity<>(allBooks,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable("id")Integer id){
        BookDto bookById = this.bookService.getBookById(id);
        return new ResponseEntity<>(bookById,HttpStatus.OK);

    }
}
