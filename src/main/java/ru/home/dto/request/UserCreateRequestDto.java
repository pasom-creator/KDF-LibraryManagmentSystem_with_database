package ru.home.dto.request;

import ru.home.model.UserType;

public record UserCreateRequestDto(String name, String email, UserType type) {
}
