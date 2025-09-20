package ru.home.repository;

import ru.home.dto.request.UserCreateRequestDto;
import ru.home.dto.response.BookBorrowedResponseDto;
import ru.home.dto.response.UserByIdResponseDto;
import ru.home.dto.response.UserByTypeResponseDto;
import ru.home.dto.response.UserInfoResponseDto;
import ru.home.dto.response.UserTypeByIdResponseDto;
import ru.home.exception.DataBaseConnectionException;
import ru.home.model.UserType;
import ru.home.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserRepo {
    private final String USER_TABLE = "users";
    private final Connection connection;
    private final int INVALID_ID = 0;
    private final int FIRST_INDEX = 1;
    private final int SECOND_INDEX = 2;
    private final int THIRD_INDEX = 3;

    public UserRepo() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new DataBaseConnectionException("No database connection", e);
        }
    }

    public Long createUser(UserCreateRequestDto userCreateRequestDto) {
        String queryCreateUser = "INSERT INTO %s (name, email, user_type) VALUES (?, ?, ?);".formatted(USER_TABLE);
        try (PreparedStatement statement = connection.prepareStatement(queryCreateUser, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(FIRST_INDEX, userCreateRequestDto.name());
            statement.setString(SECOND_INDEX, userCreateRequestDto.email());
            statement.setObject(THIRD_INDEX, userCreateRequestDto.type().toString(), Types.OTHER);
            statement.executeUpdate();

            ResultSet userId = statement.getGeneratedKeys();
            if (userId.next()) return userId.getLong("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0L;
    }

    public boolean removeUser(Long id) {
        String queryDeleteUser = "DELETE FROM %s WHERE id = ?;".formatted(USER_TABLE);
        try (PreparedStatement statement = connection.prepareStatement(queryDeleteUser)) {
            statement.setLong(FIRST_INDEX, id);
            return statement.executeUpdate() != INVALID_ID;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<UserByIdResponseDto> getUserInfoById(Long id) {
        String queryUserInfo = "SELECT name, email, user_type FROM %s WHERE id = ?".formatted(USER_TABLE);
        try (PreparedStatement statement = connection.prepareStatement(queryUserInfo)) {
            statement.setLong(FIRST_INDEX, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new UserByIdResponseDto(resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("user_type"),
                        new ArrayList<>()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Map<Long, UserInfoResponseDto> listAllUsers() {
        String query = """
                SELECT id,name,email,user_type,title,author,borrow_day,return_day FROM %s
                JOIN borrowed_books ON user_id = id
                JOIN books ON borrowed_books.isbn = books.isbn;
                """.formatted(USER_TABLE);
        Map<Long, UserInfoResponseDto> usersMap = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (!usersMap.containsKey(resultSet.getLong("id"))) {
                    usersMap.put(resultSet.getLong("id"),
                            new UserInfoResponseDto(
                                    resultSet.getLong("id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("email"),
                                    UserType.valueOf(resultSet.getString("user_type")),
                                    new ArrayList<>()));

                    addBorrowedBook(usersMap, resultSet);
                } else {
                    addBorrowedBook(usersMap, resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usersMap;
    }

    public Map<String, List<UserByTypeResponseDto>> sortUsersByType() {
        String querySortUsers = "SELECT id,name, email, user_type FROM %s".formatted(USER_TABLE);
        Map<String, List<UserByTypeResponseDto>> userMap = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(querySortUsers)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (!userMap.containsKey(resultSet.getString("user_type"))) {
                    List<UserByTypeResponseDto> userList = new ArrayList<>();
                    userList.add(builder(resultSet));
                    userMap.put(resultSet.getString("user_type"), userList);
                } else {
                    userMap.get(resultSet.getString("user_type")).add(builder(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userMap;
    }

    public Optional<UserTypeByIdResponseDto> getUserTypeById(Long id) {
        String query = "SELECT user_type FROM %s WHERE id = ?;".formatted(USER_TABLE);
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(FIRST_INDEX,id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return Optional.of(
                        new UserTypeByIdResponseDto(UserType.valueOf(resultSet.getString("user_type"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private UserByTypeResponseDto builder(ResultSet resultSet) throws SQLException {
        return new UserByTypeResponseDto(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("user_type"));
    }

    private static void addBorrowedBook(Map<Long, UserInfoResponseDto> usersMap, ResultSet resultSet)
            throws SQLException
    {
        usersMap.get(resultSet.getLong("id")).listBooks().add(
                new BookBorrowedResponseDto(
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        (LocalDate) resultSet.getObject("borrow_day", LocalDate.class),
                        (LocalDate) resultSet.getObject("return_day", LocalDate.class)
                )
        );
    }

}
