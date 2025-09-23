package ru.home.controller;

import ru.home.dto.request.UserCreateRequestDto;
import ru.home.model.UserType;
import ru.home.service.impl.UserServiceImpl;
import ru.home.util.ConsoleReader;

import java.util.Map;

public class UserControlMenu extends GeneralMenu {
    private static final String USER_CONTROL_MENU = """
            Панель управления пользователями:
            
            1 - Создать пользователя
            2 - Удалить пользователя
            3 - Получить список всех пользователей(сортировка по типу)
            4 - Получить информацию о пользователе по id
            5 - Вывести список всех пользователей
            0 - Вернуться в главное меню
            """;
    private final UserServiceImpl USER_SERVICE;

    public UserControlMenu(MainMenu menu) {
        super(USER_CONTROL_MENU);
        this.USER_SERVICE = new UserServiceImpl();
        GENERAL_MAP.put("1", this::createUser);
        GENERAL_MAP.put("2", this::deleteUser);
        GENERAL_MAP.put("3", this::sortUsersByType);
        GENERAL_MAP.put("4", this::getUserInfoById);
        GENERAL_MAP.put("5", this::listAllUsers);
        GENERAL_MAP.put("0", menu::mainMenu);
    }

    private void createUser() {
        try {
            USER_SERVICE.createUser(builderUserCreateRequestDto());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser() {
        try {
            USER_SERVICE.removeUser(enterUserId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sortUsersByType() {
        USER_SERVICE.sortUsersByType();
    }

    private void getUserInfoById() {
        try {
            USER_SERVICE.getUserInfoById(enterUserId());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listAllUsers() {
        USER_SERVICE.listAllUsers();
    }


    private UserCreateRequestDto builderUserCreateRequestDto() {
        String USER_TYPE_MENU = """
                Выберите тип пользователя: 
                    1 - Guest
                    2 - Student
                    3 - Faculty
                """;
        Map<Integer, UserType> userTypeMap = Map.of(
                1, UserType.GUEST,
                2, UserType.STUDENT,
                3, UserType.FACULTY
        );
        int userTypeChoice = 0;

        String name = ConsoleReader.askQuestion("Введите имя пользователя: ");
        String email = ConsoleReader.askQuestion("Введите email пользователя: ");

        while (true) {
            try {
                userTypeChoice = Integer.parseInt(ConsoleReader.askQuestion(USER_TYPE_MENU));
                break;
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }

        return new UserCreateRequestDto(name, email, userTypeMap.get(userTypeChoice));
    }

    private Long enterUserId() throws NumberFormatException {
        return Long.parseLong(ConsoleReader.askQuestion("Введите id пользователя: "));
    }
}
