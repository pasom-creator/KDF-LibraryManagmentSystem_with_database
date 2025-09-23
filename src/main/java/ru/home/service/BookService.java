package ru.home.service;

import ru.home.dto.request.BookCreateRequestDto;
import ru.home.dto.response.BookDataResponseDto;

public interface BookService {
    void createBook(BookCreateRequestDto bookCreateRequestDto);

    void listAllBooks();

    void listBookAuthors();

    BookDataResponseDto searchBookByParameter(String parameter, String input);

    boolean removeBook(String isbn);
}
