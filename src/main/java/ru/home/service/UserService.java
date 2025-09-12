package ru.home.service;

import ru.home.dto.request.UserCreateRequestDto;
import ru.home.dto.request.UserUpdateRequestDto;
import ru.home.dto.response.UserByIdResponseDto;
import ru.home.dto.response.UserPrintAllResponseDto;

import java.util.List;

public interface UserService {
    Long createUser(UserCreateRequestDto userCreateRequestDto);

    UserByIdResponseDto getUserInfoById (Long id);

    List<UserPrintAllResponseDto> printAllUsers();

    boolean removeUser(Long id);

    boolean updateUserInfo(Long id, UserUpdateRequestDto userUpdateRequestDto);

}
