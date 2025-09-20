package ru.home.controller;

import ru.home.dto.request.UserIdTypeRequestDto;
import ru.home.service.impl.BookServiceImpl;
import ru.home.service.impl.UserServiceImpl;
import ru.home.util.ConsoleReader;

public class LibraryServiceMenu extends GeneralMenu{
    private final static String LIBRARY_MENU = """
            
            1 - Взять книгу
            2 - Вернуть книгу
            3 - Список просроченных книг
            0 - Взврат в главное меню
            """;
    private final UserServiceImpl USER_SERVICE;
    private final BookServiceImpl BOOR_SERVICE;

    public LibraryServiceMenu(MainMenu menu) {
        super(LIBRARY_MENU);
        this.USER_SERVICE = new UserServiceImpl();
        this.BOOR_SERVICE = new BookServiceImpl();
        GENERAL_MAP.put("1",this::borrowBook);
        GENERAL_MAP.put("2",this::returnBook);
        GENERAL_MAP.put("3",this::listOverdueBooks);
        GENERAL_MAP.put("0",menu::mainMenu);
    }

    private void borrowBook() {
        String isbn = getIsbn();
        Long userId = Long.parseLong(ConsoleReader.askQuestion("Введите id пользователя: "));
        BOOR_SERVICE.borrowBook(isbn,builder(userId));
    }

    private void returnBook() {
        String isbn = getIsbn();
        BOOR_SERVICE.returnBook(isbn);
    }

    private void listOverdueBooks() {
        System.out.println("Test overdue book");
    }

    private static String getIsbn() {
        return ConsoleReader.askQuestion("Введите isbn книги: ");
    }

    private UserIdTypeRequestDto builder(Long userId) {
        return new UserIdTypeRequestDto(userId,USER_SERVICE.getUserTypeById(userId));
    }
}
