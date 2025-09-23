package ru.home.service.impl;

import ru.home.dto.request.UserIdTypeRequestDto;
import ru.home.dto.response.BookBorrowedResponseDto;
import ru.home.dto.response.UserTypeByIdResponseDto;
import ru.home.model.UserType;
import ru.home.repository.BookRepo;
import ru.home.repository.UserRepo;
import ru.home.service.LibraryService;
import ru.home.validation.UserValidator;

import java.util.List;
import java.util.Optional;

public class LibraryServiceImpl implements LibraryService {
    private final BookRepo BOOK_STORE;
    private final UserRepo USER_STORAGE;

    public LibraryServiceImpl() {
        this.BOOK_STORE = new BookRepo();
        this.USER_STORAGE = new UserRepo();
    }

    @Override
    public void borrowBook(String isbn, UserIdTypeRequestDto userIdTypeRequestDto) {
        if(USER_STORAGE.checkUserBookLimit(userIdTypeRequestDto)) {
            if (BOOK_STORE.borrowBook(isbn,userIdTypeRequestDto)) {
                System.out.printf("Вы успешно взяли книгу с isbn %s\n", isbn);
            } else {
                System.out.printf("Книга с isbn %s не доступна\n",isbn);
            }
        } else {
            System.out.println("Вы больше не можете брать книги");
        }
    }

    @Override
    public void returnBook(String isbn) {
        if (BOOK_STORE.returnBook(isbn)) {
            System.out.printf("Вы успешно вернули книгу с isbn %s\n",isbn);
        } else {
            System.out.printf("Не удалось вернуть книгу с isbn %s\n",isbn);
        }
    }

    @Override
    public void searchBorrowedBooksByUserId(Long id) {
        UserValidator.userIdValidator(id);
        List<BookBorrowedResponseDto> booksList = BOOK_STORE.searchBorrowedBooksByUserId(id);
        if(!booksList.isEmpty()) {
            for (BookBorrowedResponseDto book : booksList) {
                System.out.println(book);
            }
        } else {
            System.out.println("Пользователь не брал никаких книг");
        }

    }

    @Override
    public void listOverdueBooks() {
        List<BookBorrowedResponseDto> bookList = BOOK_STORE.listOverdueBooks();
        if(!bookList.isEmpty()) {
            for (BookBorrowedResponseDto book : bookList) {
                System.out.println(book);
            }
        } else {
            System.out.println("Нету просроченых книг");
        }
    }

    public UserType getUserTypeById(Long id) {
        UserValidator.userIdValidator(id);
        Optional<UserTypeByIdResponseDto> optUserType = USER_STORAGE.getUserTypeById(id);
        if(optUserType.isPresent()) {
            return optUserType.get().type();
        }
        throw new IllegalArgumentException("Пользателя с таким id не существует");
    }
}
