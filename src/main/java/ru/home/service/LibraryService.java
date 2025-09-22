package ru.home.service;

import ru.home.dto.request.UserIdTypeRequestDto;

public interface LibraryService {
    void borrowBook(String isbn, UserIdTypeRequestDto userIdTypeRequestDto);

    void returnBook(String isbn);

    void searchBorrowedBooksByUserId(Long id);
}
