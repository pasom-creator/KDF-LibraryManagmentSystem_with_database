package ru.home.dto.response;

import java.time.LocalDate;

public record BookBorrowedResponseDto(String title, String author, LocalDate borrowDate, LocalDate returnDate) {
}
