package ru.home.validation;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UserValidator {
    private UserValidator() {
        throw new UnsupportedOperationException("Нельзя создать экземпляр утильного класса");
    }

    public static void userNameValidator(String name) {
        if (!Objects.nonNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException("Имя пользователя не должно быть более 100 символов");
        }

        System.out.println("Валидация имени юзера прошла успешно"); // debug level in logger
    }

    public static void userEmailValidator(String email) {
        Pattern emailPattern = Pattern.compile("^\\w+@\\w+\\.[a-z]{2,3}");
        Matcher emailMatch = emailPattern.matcher(email);
        if (emailMatch.matches()) {
            System.out.println("Email введён верно");
        } else {
            throw new IllegalArgumentException("Не верный формат email");
        }
    }

    public static void userIdValidator(Long id) {
        if (Objects.nonNull(id) && id <= 0L) {
            throw new IllegalArgumentException("Id пользователя должно быть больше 0");
        }
        System.out.println("Валидация id пользователя прошла успешно");
    }
}
