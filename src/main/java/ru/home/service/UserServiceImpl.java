package ru.home.service;

import ru.home.dto.request.UserCreateRequestDto;
import ru.home.dto.request.UserUpdateRequestDto;
import ru.home.dto.response.BookBorrowedResponseDto;
import ru.home.dto.response.UserByIdResponseDto;
import ru.home.dto.response.UserByTypeResponseDto;
import ru.home.dto.response.UserPrintAllResponseDto;
import ru.home.repository.UserRepo;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private final UserRepo libraryUsers;
    private final BookServiceImpl bookService;

    public UserServiceImpl() {
        this.libraryUsers = new UserRepo();
        this.bookService = new BookServiceImpl();
    }

    @Override
    public Long createUser(UserCreateRequestDto userCreateRequestDto) {
        return libraryUsers.createUser(userCreateRequestDto);
    }

    @Override
    public UserByIdResponseDto getUserInfoById(Long id) {
        UserByIdResponseDto output = libraryUsers.getUserInfoById(id);
        List<BookBorrowedResponseDto> listBooks = bookService.getUserBooksByUserId(id);
        for (BookBorrowedResponseDto book : listBooks) {
            output.listBooks().add(book);
        }
        return output;
    }

    @Override
    public List<UserPrintAllResponseDto> printAllUsers() {
        List<UserPrintAllResponseDto> listUsers = libraryUsers.printAllUsers();
        for (UserPrintAllResponseDto user : listUsers) {
            user.listBooks().addAll(bookService.getUserBooksByUserId(user.id()));
        }
        return listUsers;
    }

    @Override
    public boolean removeUser(Long id) {
        return libraryUsers.removeUser(id);
    }

    @Override
    public boolean updateUserInfo(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        return libraryUsers.updateUserInfo(id, userUpdateRequestDto);
    }

    public Map<String, List<UserByTypeResponseDto>> sortUsersByType() {
        return libraryUsers.sortUsersByType();
    }
}
