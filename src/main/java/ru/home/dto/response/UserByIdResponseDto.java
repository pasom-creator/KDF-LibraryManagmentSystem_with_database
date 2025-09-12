package ru.home.dto.response;

import java.util.List;

public record UserByIdResponseDto(String name, String email, String type, List<BookBorrowedResponseDto> listBooks) {
}
