package ru.home.service.impl;

import ru.home.dto.request.BookCreateRequestDto;
import ru.home.dto.response.BookAuthorResponseDto;
import ru.home.dto.response.BookDataResponseDto;
import ru.home.repository.BookRepo;
import ru.home.service.BookService;
import ru.home.validation.BookValidator;

import java.util.List;
import java.util.Set;

public class BookServiceImpl implements BookService {
    private final BookRepo BOOK_STORE;

    public BookServiceImpl() {
        this.BOOK_STORE = new BookRepo();
    }

    @Override
    public void createBook(BookCreateRequestDto bookCreateRequestDto) {
        if (BookValidator.bookDetailsValidator(
                bookCreateRequestDto.isbn(),
                bookCreateRequestDto.title(),
                bookCreateRequestDto.author())) {
            Long bookId = BOOK_STORE.creatBook(bookCreateRequestDto);
            if (bookId != 0) {
                System.out.printf("Книга с id %d успешно добавлена\n", bookId);
            } else {
                System.out.println("Произошла ошибка при добавление книги. Повторите попытку");
            }
        } else {
            System.out.println("Поля не могут быть пустыми");
        }
    }

    @Override
    public boolean removeBook(String isbn) {
        if (BookValidator.fieldValidator(isbn)) {
            return BOOK_STORE.removeBook(isbn);
        } else {
            throw new IllegalArgumentException("Поле не может быть пустым");
        }

    }

    @Override
    public void listAllBooks() {
        List<BookDataResponseDto> listBooks = BOOK_STORE.listAllBooks();
        if (!listBooks.isEmpty()) {
            for (BookDataResponseDto book : listBooks) {
                System.out.println(book);
            }
        } else {
            System.out.println("Список книг пуст");
        }
    }

    @Override
    public void listBookAuthors() {
        Set<BookAuthorResponseDto> authorSet = BOOK_STORE.listBookAuthors();
        if (!authorSet.isEmpty()) {
            for (BookAuthorResponseDto author : authorSet) {
                System.out.println(author);
            }
        } else {
            System.out.println("Список книг пуст");
        }
    }

    @Override
    public BookDataResponseDto searchBookByParameter(String parameter, String input) {
        return BOOK_STORE.searchBookByParameter(parameter, input);
    }

}
