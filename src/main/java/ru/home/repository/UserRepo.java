package ru.home.repository;

import ru.home.dto.request.UserCreateRequestDto;
import ru.home.dto.request.UserUpdateRequestDto;
import ru.home.dto.response.UserByIdResponseDto;
import ru.home.dto.response.UserByTypeResponseDto;
import ru.home.dto.response.UserPrintAllResponseDto;
import ru.home.exception.DataBaseConnectionException;
import ru.home.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public boolean updateUserInfo(Long id, UserUpdateRequestDto request) {
        String queryUpdateUserInfo = "UPDATE %s SET name = ?, email = ? WHERE id = ?;".formatted(USER_TABLE);
        try (PreparedStatement statement = connection.prepareStatement(queryUpdateUserInfo)) {
            statement.setString(FIRST_INDEX, request.name());
            statement.setString(SECOND_INDEX, request.email());
            statement.setLong(THIRD_INDEX, id);
            return statement.executeUpdate() != INVALID_ID;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserByIdResponseDto getUserInfoById(Long id) {
        String queryUserInfo = "SELECT name, email, user_type FROM %s WHERE id = ?".formatted(USER_TABLE);
        UserByIdResponseDto user = null;

        try (PreparedStatement statement = connection.prepareStatement(queryUserInfo)) {
            statement.setLong(FIRST_INDEX, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new UserByIdResponseDto(resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("user_type"),
                        new ArrayList<>());
            } else {
                throw new IllegalArgumentException("No user in library with id %d".formatted(id));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public List<UserPrintAllResponseDto> printAllUsers() {
        String query ="SELECT id, name, email, user_type FROM %s;".formatted(USER_TABLE);
        List<UserPrintAllResponseDto> listUsers = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                listUsers.add(
                        new UserPrintAllResponseDto(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("user_type"),
                                new ArrayList<>())
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listUsers;
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

    private UserByTypeResponseDto builder(ResultSet resultSet) throws SQLException {
        return new UserByTypeResponseDto(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("user_type"));
    }

}
