package ru.home.dto.response;

import ru.home.model.UserType;

public record UserByTypeResponseDto(Long id, String name, String email, String type) {
}
