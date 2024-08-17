package org.example.library.serviceImplementation;

import org.example.library.dto.BookDto;
import org.example.library.dto.StudentDto;
import org.example.library.entities.Book;
import org.example.library.exceptions.ApiException;
import org.example.library.exceptions.ResourceNotFoundException;
import org.example.library.repositories.BookRepository;
import org.example.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImp implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BookDto createBook(BookDto bookDto) {
        try {
            Book book = modelMapper.map(bookDto, Book.class);
            if (this.bookRepository.existsByBookId(book.getBookId())) {
                throw new DataIntegrityViolationException("A book with the provided bookid already exists.");
            }
            Book saveBook = this.bookRepository.save(book);
            return modelMapper.map(saveBook,BookDto.class);
        } catch (DataIntegrityViolationException ex) {
            // Exception indicates duplicate entry
            String errorMessage = ex.getMessage();
            if (errorMessage.contains("bookid")) {
                throw new ApiException("A book with the provided bookid already exists.");

            }


        }
        catch (Exception e){
            throw  new ApiException("invalid Book Details");
        }
        return null;
    }

    @Override
    public BookDto getBookById(Integer id) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("book", "bookId", id));
        BookDto map = modelMapper.map(book, BookDto.class);
        map.setStudentDto(modelMapper.map(book.getStudent(), StudentDto.class));
        return map;




    }

    @Override
    public BookDto getBookByName() {
        return null;
    }

    @Override
    public List<BookDto> getAllBooks() {

        List<Book> all = this.bookRepository.findAll();
        List<BookDto> booksDto = all.stream().map((book) -> {
            StudentDto studentDto = this.modelMapper.map(book.getStudent(), StudentDto.class);
            BookDto bookDto = this.modelMapper.map(book, BookDto.class);
            bookDto.setStudentDto(studentDto);
            return bookDto;
        }).collect(Collectors.toList());
        return booksDto;
    }
}
