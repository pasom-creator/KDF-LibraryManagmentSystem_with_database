package ru.home.dto.response;

import ru.home.model.UserType;

import java.util.List;

public record UserInfoResponseDto(Long id, String name, String email, UserType type,
                                  List<BookBorrowedResponseDto> listBooks) {
}
