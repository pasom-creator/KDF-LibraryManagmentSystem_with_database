package ru.home.model;

public enum UserType {
    GUEST(1), STUDENT(3), FACULTY(10);

    private final int bookAmount;

    UserType(int bookAmount) {
        this.bookAmount = bookAmount;
    }

    public int getBookAmount() {
        return bookAmount;
    }
}
