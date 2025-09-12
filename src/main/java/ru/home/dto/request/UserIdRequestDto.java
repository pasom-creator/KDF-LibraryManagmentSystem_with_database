package ru.home.dto.request;

import ru.home.model.UserType;

public record UserIdRequestDto(Long userId, UserType type) {
}
