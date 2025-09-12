package ru.home.dto.response;

import java.util.List;

public record UserPrintAllResponseDto(Long id, String name, String email, String type,
                                      List<BookBorrowedResponseDto> listBooks) {
}
