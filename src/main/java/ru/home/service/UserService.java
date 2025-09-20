package ru.home.service;

import ru.home.dto.request.UserCreateRequestDto;
import ru.home.dto.request.UserUpdateRequestDto;
import ru.home.dto.response.UserByIdResponseDto;

public interface UserService {
    void createUser(UserCreateRequestDto userCreateRequestDto);

    void getUserInfoById (Long id);

    void listAllUsers();

    void removeUser(Long id);

    boolean updateUserInfo(Long id, UserUpdateRequestDto userUpdateRequestDto);

}
