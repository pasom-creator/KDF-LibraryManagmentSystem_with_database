package ru.home.controller;

public class MainMenu extends GeneralMenu{
    private static final String MAIN_MENU = """
            1 - Панель управления пользователями
            2 - Панель управления книгами
            3 - Услуги библиотеки
            0 - Выход
            """;

    public MainMenu() {
        super(MAIN_MENU);
        GENERAL_MAP.put("1",this::userControlMenu);
        GENERAL_MAP.put("2",this::bookControlMenu);
        GENERAL_MAP.put("3",this::libraryServiceMenu);
        GENERAL_MAP.put("0", ()->{
            System.out.println("Завершение работы программы");
            System.exit(0);
        });
    }

    public void mainMenu() {
        start();
    }

    private void userControlMenu() {
        new UserControlMenu(this).start();
    }

    private void bookControlMenu() {
        new BookControlMenu(this).start();
    }

    private void libraryServiceMenu() {
        new LibraryServiceMenu(this).start();
    }

}
