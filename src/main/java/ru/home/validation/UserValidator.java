package ru.home.validation;

import java.util.Objects;

public final class UserValidator {
    private UserValidator() {
        throw new UnsupportedOperationException("Нельзя создать экземпляр утильного класса");
    }

    public static void userNameValidator(String name) {
        if(Objects.nonNull(name) && name.length() > 1 && name.length() <= 100) {
            throw new IllegalArgumentException("Имя пользователя не должно быть более 100 символов");
        }
        System.out.println("валидация имени юзера прошла успешно"); // debug level in logger
    }

    public static void userId(Long id) {
        if(Objects.nonNull(id) && id != 0L) {
            throw new IllegalArgumentException("Id user can't be 0");
        }
        System.out.println("Валидация прошла успешно");
    }
}
