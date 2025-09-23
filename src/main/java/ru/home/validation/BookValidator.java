package ru.home.validation;

import java.util.Objects;

public final class BookValidator {
    private BookValidator() {
        throw new UnsupportedOperationException("Нельзя создать экземпляр утильного класса");
    }

    public static boolean fieldValidator(String input) {
        return Objects.nonNull(input) && input.isEmpty();
    }

    public static boolean bookDetailsValidator(String isbn, String title, String author) {
        return fieldValidator(isbn) && fieldValidator(title) && fieldValidator(author);
    }
}
