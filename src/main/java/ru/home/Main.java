package ru.home;

import ru.home.dto.request.BookAddRequestDto;
import ru.home.dto.request.UserCreateRequestDto;
import ru.home.dto.response.UserByTypeResponseDto;
import ru.home.dto.response.UserPrintAllResponseDto;
import ru.home.model.UserType;
import ru.home.service.BookServiceImpl;
import ru.home.service.UserServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
//        BookServiceImpl library = new BookServiceImpl();
//        runApp(library);

        UserServiceImpl libraryUsers = new UserServiceImpl();
        userApp(libraryUsers);
    }

    private static void userApp(UserServiceImpl service) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            int input = 0;

            String menu = """
                    Панель управления пользователями:
                    
                    1 - Создать пользователя
                    2 - Удалить пользователя
                    3 - Получить список пользователей(сортировка по типу)
                    4 - Получить список книг пользователя
                    5 - Получить список всех пользователей и их книг
                    6 - Выход
                    """;
            System.out.println(menu);

            while (input != 6) {
                System.out.println("Enter number: ");
                input = Integer.parseInt(reader.readLine().strip());

                switch (input) {
                    case 1 -> {
                        System.out.println("Enter user name: ");
                        String name = reader.readLine().strip();
                        System.out.println("Enter user email: ");
                        String email = reader.readLine().strip();
                        System.out.println("""
                                Choose user type: 
                                    1 - Guest
                                    2 - Student
                                    3 - Faculty
                                """);
                        UserType type = switch (Integer.parseInt(reader.readLine().strip())) {
                            case 1 -> UserType.GUEST;
                            case 2 -> UserType.STUDENT;
                            case 3 -> UserType.FACULTY;
                            default -> throw new IllegalStateException(
                                    "Unexpected value: " + Integer.parseInt(reader.readLine().strip()));
                        };
                        System.out.printf("User is successfully created with ID %d\n",
                                service.createUser(new UserCreateRequestDto(name, email, type)));
                    }
                    case 2 -> {
                        System.out.println("Enter user id to delete: ");
                        Long id = Long.parseLong(reader.readLine().strip());
                        System.out.println(service.removeUser(id));
                    }
                    case 3 -> {
                        Map<String, List<UserByTypeResponseDto>> map = service.sortUsersByType();
                        for (Map.Entry<String, List<UserByTypeResponseDto>> user : map.entrySet()) {
                            System.out.println(user.getKey() + " : " + user.getValue());
                        }
                    }
                    case 4 -> {
                        System.out.println("Enter user id: ");
                        long id = Long.parseLong(reader.readLine());
                        System.out.println(service.getUserInfoById(id));
                    }
                    case 5 -> {
                        for (UserPrintAllResponseDto user : service.printAllUsers()) {
                            System.out.println(user);
                        }
                    }
                    case 6 -> {
                        System.out.println("Terminating program");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Валидации применяются на уровне получения информации(контроллера)
    private static void runApp(BookServiceImpl service) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            int input = 0;

            String menu = """
                    Панель управления приложением:
                    
                    1 - Добавить книгу
                    2 - Удалить книгу
                    3 - Получить получить список всех книг в библиотеке
                    4 - Получить список всех авторов
                    6 - Выход
                    
                    Ведите число для выполнения операции
                    """;
            System.out.println(menu);

            while (input != 6) {
                System.out.print("Enter number: ");
                input = Integer.parseInt(reader.readLine().strip());

                switch (input) {
                    case 1 -> {
                        System.out.println("Введите isbn книги: ");
                        String isbn = reader.readLine();
                        System.out.println("Введите название книги: ");
                        String title = reader.readLine();
                        System.out.println("Введите автора книги: ");
                        String author = reader.readLine();
                        service.addBook(new BookAddRequestDto(isbn, title, author));
                    }
                    case 2 -> {
                        System.out.println(service.removeBook("978-0-06-112122-4"));
                    }
                    case 3 -> {
                        System.out.println(service.listAllBooks());
                    }
                    case 4 -> {
                        System.out.println(service.listBookAuthors());
                    }
                    case 6 -> {
                        System.out.println("Terminating program");
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
