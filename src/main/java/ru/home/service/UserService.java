package ru.home.service;

import ru.home.dto.request.UserCreateRequestDto;

public interface UserService {
    void createUser(UserCreateRequestDto userCreateRequestDto);

    void getUserInfoById (Long id);

    void listAllUsers();

    void removeUser(Long id);
}
