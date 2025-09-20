package ru.home.service.impl;

import ru.home.dto.request.BookCreateRequestDto;
import ru.home.dto.request.UserIdTypeRequestDto;
import ru.home.dto.response.BookAuthorResponseDto;
import ru.home.dto.response.BookBorrowedResponseDto;
import ru.home.dto.response.BookDataResponseDto;
import ru.home.repository.BookRepo;
import ru.home.service.BookService;
import ru.home.service.LibraryService;

import java.util.List;
import java.util.Set;

public class BookServiceImpl implements BookService, LibraryService {
    private final BookRepo bookStore;

    public BookServiceImpl() {
        this.bookStore = new BookRepo();
    }

    @Override
    public void addBook(BookCreateRequestDto bookCreateRequestDto) {
        Long bookId = bookStore.creatBook(bookCreateRequestDto);
        if (bookId != 0) {
            System.out.printf("Книга с id %d успешно добавлена", bookId);
        } else {
            System.out.println("Произошла ошибка при добавление книги. Повторите попытку");
        }
    }

    @Override
    public boolean removeBook(String isbn) {
        return bookStore.removeBook(isbn);
    }

    @Override
    public List<BookDataResponseDto> listAllBooks() {
        return bookStore.listAllBooks();
    }

    @Override
    public Set<BookAuthorResponseDto> listBookAuthors() {
        return bookStore.listBookAuthors();
    }

    @Override
    public BookDataResponseDto searchBookByParameter(String parameter, String input) {
        return bookStore.searchBookByParameter(parameter, input);
    }

    @Override
    public void borrowBook(String isbn, UserIdTypeRequestDto userIdTypeRequestDto) {
        if (bookStore.borrowBook(isbn,userIdTypeRequestDto)) {
            System.out.printf("Вы успешно взяли книгу с isbn %s\n", isbn);
        } else {
            System.out.printf("Не удалось взять книгу с isbn %s\n",isbn);
        }
    }

    @Override
    public void returnBook(String isbn) {
        if (bookStore.returnBook(isbn)) {
            System.out.printf("Вы успешно вернули книгу с isbn %s\n",isbn);
        } else {
            System.out.printf("Не удалось вернуть книгу с isbn %s\n",isbn);
        }
    }

    public List<BookBorrowedResponseDto> getUserBooksByUserId(Long id) {
        return bookStore.getUserBooksByUserId(id);
    }


}
