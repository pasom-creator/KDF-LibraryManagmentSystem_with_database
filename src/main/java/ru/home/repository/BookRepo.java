package ru.home.repository;

import ru.home.dto.request.BookAddRequestDto;
import ru.home.dto.request.UserIdRequestDto;
import ru.home.dto.response.BookAuthorResponseDto;
import ru.home.dto.response.BookAvailableResponseDto;
import ru.home.dto.response.BookBorrowedResponseDto;
import ru.home.dto.response.BookDataResponseDto;
import ru.home.exception.DataBaseConnectionException;
import ru.home.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BookRepo {
    private final String BOOK_TABLE = "books";
    private final Connection connection;
    private final int INVALID_ID = 0;
    private final int FIRST_INDEX = 1;
    private final int SECOND_INDEX = 2;
    private final int THIRD_INDEX = 3;

    public BookRepo() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new DataBaseConnectionException("No database connection", e);
        }
    }

    public void addBook(BookAddRequestDto bookAddRequestDto) {
        String queryAddBook = "INSERT INTO %s (isbn, title, author) VALUES (?, ?, ?);".formatted(BOOK_TABLE);
        try (PreparedStatement statement = connection.prepareStatement(queryAddBook)) {
            statement.setString(FIRST_INDEX, bookAddRequestDto.isbn());
            statement.setString(SECOND_INDEX, bookAddRequestDto.title());
            statement.setString(THIRD_INDEX, bookAddRequestDto.author());
            statement.executeUpdate();
            System.out.println("Книга успешно добавлена в библиотеку");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeBook(String isbn) {
        String queryRemoveBook = "DELETE FROM %s WHERE isbn = ?".formatted(BOOK_TABLE);
        try (PreparedStatement statement = connection.prepareStatement(queryRemoveBook)) {
            statement.setString(FIRST_INDEX, isbn);
            return statement.executeUpdate() != INVALID_ID;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BookDataResponseDto> listAllBooks() {
        List<BookDataResponseDto> libraryBooks = new ArrayList<>();
        String queryAllBooks = "SELECT isbn, author,title FROM %s".formatted(BOOK_TABLE);
        try (PreparedStatement statement = connection.prepareStatement(queryAllBooks)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                libraryBooks.add(builder(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return libraryBooks;
    }

    public Set<BookAuthorResponseDto> listBookAuthors() {
        Set<BookAuthorResponseDto> authorsList = new HashSet<>();
        String queryBooksByAuthor = "SELECT author FROM %s".formatted(BOOK_TABLE);
        try (PreparedStatement statement = connection.prepareStatement(queryBooksByAuthor)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                authorsList.add(new BookAuthorResponseDto(resultSet.getString("author")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authorsList;
    }

    public BookDataResponseDto searchBookByParameter(String parameter, String input) {
        String querySearchByParameter = "SELECT isbn, title, author FROM %s WHERE ? = ?;".formatted(BOOK_TABLE);
        try (PreparedStatement statement = connection.prepareStatement(querySearchByParameter)) {
            statement.setString(FIRST_INDEX, parameter);
            statement.setString(SECOND_INDEX, input);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? builder(resultSet) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BookBorrowedResponseDto> getUserBooksByUserId(Long id) {
        List<BookBorrowedResponseDto> listBooks = new ArrayList<>();
        String queryBorrowedBooks = """
                SELECT books.title, books.author, borrowed_books.borrow_day, borrowed_books.return_day
                FROM borrowed_books
                JOIN books ON borrowed_books.isbn = books.isbn
                JOIN users ON borrowed_books.user_id = users.id
                WHERE borrowed_books.user_id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(queryBorrowedBooks)) {
            statement.setLong(FIRST_INDEX, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                listBooks.add(new BookBorrowedResponseDto(
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getObject("borrow_day", LocalDate.class),
                        resultSet.getObject("return_day", LocalDate.class)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listBooks;
    }

    public void borrowBook(String title, String author, UserIdRequestDto userIdRequestDto) {
        BookAvailableResponseDto book = isBookAvailable(title, author);
        if (Objects.nonNull(book)) {
            String query = "INSERT INTO borrowed_books (user_id, isbn, borrow_day, return_day) VALUES (?, ?, ?, ?);";
            String queryUnAvailable = "UPDATE %s SET available = ? WHERE ibsn = ?".formatted(BOOK_TABLE);
            try (PreparedStatement statement = connection.prepareStatement(query);
                 PreparedStatement statement2 = connection.prepareStatement(queryUnAvailable)) {
                connection.setAutoCommit(false);
                statement.setLong(FIRST_INDEX, userIdRequestDto.userId());
                statement.setString(SECOND_INDEX, book.isbn());
                statement.setObject(THIRD_INDEX, LocalDate.now());
                statement.setObject(4, LocalDate.now().plusDays(7));
                statement.executeUpdate();

                statement2.setBoolean(FIRST_INDEX, false);
                statement2.setString(SECOND_INDEX, book.isbn());
                statement2.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                if(connection !=null) {
                    try {
                        connection.rollback();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                throw new RuntimeException(e);
            }
        }
    }

    public BookAvailableResponseDto isBookAvailable(String title, String author) {
        String query = "SELECT title, author, available FROM %s WHERE title = ? AND author = ?".formatted(BOOK_TABLE);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(FIRST_INDEX, title);
            statement.setString(SECOND_INDEX, author);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new BookAvailableResponseDto(resultSet.getString("isbn"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static BookDataResponseDto builder(ResultSet resultSet) throws SQLException {
        return new BookDataResponseDto(
                resultSet.getString("isbn"),
                resultSet.getString("author"),
                resultSet.getString("title"));
    }
}
