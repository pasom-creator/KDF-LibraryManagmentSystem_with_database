package ru.home.controller;

import ru.home.dto.request.BookCreateRequestDto;
import ru.home.service.impl.BookServiceImpl;
import ru.home.util.ConsoleReader;

public class BookControlMenu extends GeneralMenu {
    private final static String CONTROL_MENU = """
            
            1 - Добавить книгу
            2 - Удалить книгу
            3 - Вывести список всех книг
            4 - Вывести список всех авторов
            0 - Взврат в главное меню
            """;
    private final BookServiceImpl BOOK_SERVICE;

    public BookControlMenu(MainMenu menu) {
        super(CONTROL_MENU);
        this.BOOK_SERVICE = new BookServiceImpl();
        GENERAL_MAP.put("1", this::addBook);
        GENERAL_MAP.put("2", this::deleteBook);
        GENERAL_MAP.put("3", this::listBooks);
        GENERAL_MAP.put("4", this::listAuthors);
        GENERAL_MAP.put("0", menu::mainMenu);
    }

    private void addBook() {
        BOOK_SERVICE.createBook(builder());
    }

    private void deleteBook() {
        try {
            String isbn = ConsoleReader.askQuestion("Введите isbn книги: ");
            System.out.println(BOOK_SERVICE.removeBook(isbn));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listBooks() {
        BOOK_SERVICE.listAllBooks();
    }

    private void listAuthors() {
        BOOK_SERVICE.listBookAuthors();
    }

    private BookCreateRequestDto builder() {
        return new BookCreateRequestDto(
                ConsoleReader.askQuestion("Введите isbn книги: "),
                ConsoleReader.askQuestion("Введите название книги: "),
                ConsoleReader.askQuestion("Введите автора книги: "));
    }
}
