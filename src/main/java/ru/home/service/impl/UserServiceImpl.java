package ru.home.service.impl;

import ru.home.dto.request.UserCreateRequestDto;
import ru.home.dto.response.UserByIdResponseDto;
import ru.home.dto.response.UserByTypeResponseDto;
import ru.home.dto.response.UserInfoResponseDto;
import ru.home.repository.UserRepo;
import ru.home.service.UserService;
import ru.home.validation.UserValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepo USER_STORAGE;

    public UserServiceImpl() {
        this.USER_STORAGE = new UserRepo();
    }

    @Override
    public void createUser(UserCreateRequestDto userCreateRequestDto) {
        UserValidator.userNameValidator(userCreateRequestDto.name());
        UserValidator.userEmailValidator(userCreateRequestDto.email());
        System.out.printf("Создан пользователь с id %d\n", USER_STORAGE.createUser(userCreateRequestDto));
    }

    @Override
    public void removeUser(Long id) {
        UserValidator.userIdValidator(id);
        if (USER_STORAGE.removeUser(id)) {
            System.out.printf("Пользователь с id %d успешно удалён\n", id);
        }

        System.out.printf("Пользователь с id %d не найден в системе\n", id);
    }

    @Override
    public void getUserInfoById(Long id) {
        UserValidator.userIdValidator(id);
        Optional<UserByIdResponseDto> optUser = USER_STORAGE.getUserInfoById(id);
        if (optUser.isPresent()) {
            UserByIdResponseDto user = optUser.get();
            System.out.println(user);
        } else {
            System.out.printf("Пользователя с id %d не найден в системе\n", id);
        }
    }

    @Override
    public void listAllUsers() {
        Map<Long, UserInfoResponseDto> usersMap = USER_STORAGE.listAllUsers();
        for (Map.Entry<Long, UserInfoResponseDto> userEntry : usersMap.entrySet()) {
            System.out.println(userEntry.getValue());
        }
    }

    public void sortUsersByType() {
        if (!USER_STORAGE.sortUsersByType().isEmpty()) {
            for (Map.Entry<String, List<UserByTypeResponseDto>> userEntry : USER_STORAGE.sortUsersByType().entrySet()) {
                System.out.println(userEntry.getKey() + " : " + userEntry.getValue());
            }
        } else {
            System.out.println("Список пользователей пуст");
        }
    }
}
