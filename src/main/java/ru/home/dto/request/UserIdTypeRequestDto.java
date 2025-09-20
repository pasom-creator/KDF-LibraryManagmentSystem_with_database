package ru.home.dto.request;

import ru.home.model.UserType;

public record UserIdTypeRequestDto(Long userId, UserType type) {
}
