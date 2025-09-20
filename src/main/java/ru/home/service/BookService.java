package ru.home.service;

import ru.home.dto.request.BookCreateRequestDto;
import ru.home.dto.response.BookAuthorResponseDto;
import ru.home.dto.response.BookDataResponseDto;

import java.util.List;
import java.util.Set;

public interface BookService {
    void addBook(BookCreateRequestDto bookCreateRequestDto);

    List<BookDataResponseDto> listAllBooks();

    Set<BookAuthorResponseDto> listBookAuthors();

    BookDataResponseDto searchBookByParameter(String parameter, String input);

    boolean removeBook(String isbn);
}
