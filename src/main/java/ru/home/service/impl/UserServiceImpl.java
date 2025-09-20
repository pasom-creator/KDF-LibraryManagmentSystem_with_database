package ru.home.service.impl;

import ru.home.dto.request.UserCreateRequestDto;
import ru.home.dto.request.UserUpdateRequestDto;
import ru.home.dto.response.BookBorrowedResponseDto;
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
    private final UserRepo libraryUsers;
    private final BookServiceImpl bookService;

    public UserServiceImpl() {
        this.libraryUsers = new UserRepo();
        this.bookService = new BookServiceImpl();
    }

    @Override
    public void createUser(UserCreateRequestDto userCreateRequestDto) {
        UserValidator.userNameValidator(userCreateRequestDto.name());
        UserValidator.userEmailValidator(userCreateRequestDto.email());
        System.out.printf("Создан пользователь с id %d\n", libraryUsers.createUser(userCreateRequestDto));
    }

    @Override
    public void getUserInfoById(Long id) {
        UserValidator.userId(id);
        Optional<UserByIdResponseDto> optUser = libraryUsers.getUserInfoById(id);
        if (optUser.isPresent()) {
            UserByIdResponseDto user = optUser.get();
            System.out.println(user);
        } else {
            System.out.printf("Пользователя с id %d\n", id);
        }
    }

    @Override
    public void listAllUsers() {
        Map<Long, UserInfoResponseDto> usersMap = libraryUsers.listAllUsers();
        for (Map.Entry<Long, UserInfoResponseDto> userEntry : usersMap.entrySet()) {
            System.out.println(userEntry.getValue());
        }
    }

    @Override
    public void removeUser(Long id) {
        UserValidator.userId(id);
        if (libraryUsers.removeUser(id)) {
            System.out.printf("Пользователь с id %d успешно удалён\n", id);
        }

        System.out.printf("Пользователь с id %d не найден в системе\n", id);
    }

    @Override
    public boolean updateUserInfo(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        return libraryUsers.updateUserInfo(id, userUpdateRequestDto);
    }

    public void sortUsersByType() {
        if (!libraryUsers.sortUsersByType().isEmpty()) {
            for (Map.Entry<String, List<UserByTypeResponseDto>> userEntry : libraryUsers.sortUsersByType().entrySet()) {
                System.out.println(userEntry.getKey() + " : " + userEntry.getValue());
            }
        } else {
            System.out.println("Список пользователей пуст");
        }
    }
}
