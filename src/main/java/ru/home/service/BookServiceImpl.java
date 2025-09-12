package ru.home.service;

import ru.home.dto.request.BookAddRequestDto;
import ru.home.dto.response.BookAuthorResponseDto;
import ru.home.dto.response.BookBorrowedResponseDto;
import ru.home.dto.response.BookDataResponseDto;
import ru.home.repository.BookRepo;

import java.util.List;
import java.util.Set;

public class BookServiceImpl implements BookService {
    private final BookRepo library;

    public BookServiceImpl() {
        this.library  = new BookRepo();
    }

    @Override
    public void addBook(BookAddRequestDto bookAddRequestDto) {
        library.addBook(bookAddRequestDto);
    }

    @Override
    public List<BookDataResponseDto> listAllBooks() {
        return library.listAllBooks();
    }

    @Override
    public boolean removeBook(String isbn) {
        return library.removeBook(isbn);
    }

    @Override
    public Set<BookAuthorResponseDto> listBookAuthors() {
        return library.listBookAuthors();
    }

    @Override
    public BookDataResponseDto searchBookByParameter(String parameter, String input) {
        return library.searchBookByParameter(parameter,input);
    }

    public List<BookBorrowedResponseDto> getUserBooksByUserId(Long id) {
        return library.getUserBooksByUserId(id);
    };

//    List<Book> getUserOverdueBooksByUserId(Long id);
//    boolean returnBook(String isbn);
}
