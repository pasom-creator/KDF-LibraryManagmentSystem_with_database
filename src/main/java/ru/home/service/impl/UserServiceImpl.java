package ru.home.service.impl;

import ru.home.dto.request.UserCreateRequestDto;
import ru.home.dto.response.UserByIdResponseDto;
import ru.home.dto.response.UserByTypeResponseDto;
import ru.home.dto.response.UserInfoResponseDto;
import ru.home.dto.response.UserTypeByIdResponseDto;
import ru.home.model.UserType;
import ru.home.repository.UserRepo;
import ru.home.service.UserService;
import ru.home.validation.UserValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepo userStorage;

    public UserServiceImpl() {
        this.userStorage = new UserRepo();
    }

    @Override
    public void createUser(UserCreateRequestDto userCreateRequestDto) {
        UserValidator.userNameValidator(userCreateRequestDto.name());
        UserValidator.userEmailValidator(userCreateRequestDto.email());
        System.out.printf("Создан пользователь с id %d\n", userStorage.createUser(userCreateRequestDto));
    }

    @Override
    public void removeUser(Long id) {
        UserValidator.userId(id);
        if (userStorage.removeUser(id)) {
            System.out.printf("Пользователь с id %d успешно удалён\n", id);
        }

        System.out.printf("Пользователь с id %d не найден в системе\n", id);
    }

    @Override
    public void getUserInfoById(Long id) {
        UserValidator.userId(id);
        Optional<UserByIdResponseDto> optUser = userStorage.getUserInfoById(id);
        if (optUser.isPresent()) {
            UserByIdResponseDto user = optUser.get();
            System.out.println(user);
        } else {
            System.out.printf("Пользователя с id %d\n", id);
        }
    }

    @Override
    public void listAllUsers() {
        Map<Long, UserInfoResponseDto> usersMap = userStorage.listAllUsers();
        for (Map.Entry<Long, UserInfoResponseDto> userEntry : usersMap.entrySet()) {
            System.out.println(userEntry.getValue());
        }
    }

    public void sortUsersByType() {
        if (!userStorage.sortUsersByType().isEmpty()) {
            for (Map.Entry<String, List<UserByTypeResponseDto>> userEntry : userStorage.sortUsersByType().entrySet()) {
                System.out.println(userEntry.getKey() + " : " + userEntry.getValue());
            }
        } else {
            System.out.println("Список пользователей пуст");
        }
    }

    public UserType getUserTypeById(Long id) {
        UserValidator.userId(id);
        Optional<UserTypeByIdResponseDto> optUserType = userStorage.getUserTypeById(id);
        if(optUserType.isPresent()) {
            return optUserType.get().type();
        }
        throw new IllegalArgumentException("Пользателя с таким id не существует");
    }
}
