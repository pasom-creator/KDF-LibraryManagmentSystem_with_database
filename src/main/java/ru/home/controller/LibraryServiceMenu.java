package ru.home.controller;

import ru.home.dto.request.UserIdTypeRequestDto;
import ru.home.service.impl.LibraryServiceImpl;
import ru.home.util.ConsoleReader;

public class LibraryServiceMenu extends GeneralMenu {
    private final static String LIBRARY_MENU = """
            
            1 - Взять книгу
            2 - Вернуть книгу
            3 - Список просроченных книг
            4 - Получить список книг пользоваля
            0 - Взврат в главное меню
            """;
    private final LibraryServiceImpl LIBRARY_SERVICE;

    public LibraryServiceMenu(MainMenu menu) {
        super(LIBRARY_MENU);
        this.LIBRARY_SERVICE = new LibraryServiceImpl();
        GENERAL_MAP.put("1", this::borrowBook);
        GENERAL_MAP.put("2", this::returnBook);
        GENERAL_MAP.put("3", this::listOverdueBooks);
        GENERAL_MAP.put("4", this::listBorrowedBooksByUserId);
        GENERAL_MAP.put("0", menu::mainMenu);
    }

    private void borrowBook() {
        String isbn = getIsbn();
        try {
            Long userId = getUserId();
            LIBRARY_SERVICE.borrowBook(isbn, builder(userId));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void returnBook() {
        String isbn = getIsbn();
        LIBRARY_SERVICE.returnBook(isbn);
    }

    private void listOverdueBooks() {
        LIBRARY_SERVICE.listOverdueBooks();
    }

    private void listBorrowedBooksByUserId() {
        try {
            Long userId = getUserId();
            LIBRARY_SERVICE.searchBorrowedBooksByUserId(userId);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getIsbn() {
        return ConsoleReader.askQuestion("Введите isbn книги: ");
    }

    private Long getUserId() {
        return Long.parseLong(ConsoleReader.askQuestion("Введите id пользователя: "));
    }

    private UserIdTypeRequestDto builder(Long userId) {
        return new UserIdTypeRequestDto(userId, LIBRARY_SERVICE.getUserTypeById(userId));
    }
}
