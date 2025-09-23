package ru.home.model;

import java.time.LocalDate;

public class BorrowedBook extends Book {
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowedBook(String isbn, String title, String author) {
        super(isbn, title, author);
    }

    public BorrowedBook(String title, String author, LocalDate borrowDate, LocalDate returnDate) {
        super(title, author);
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }
}
