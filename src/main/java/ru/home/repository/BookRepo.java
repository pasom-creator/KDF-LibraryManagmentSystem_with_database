package ru.home.repository;

import ru.home.dto.request.BookCreateRequestDto;
import ru.home.dto.request.UserIdTypeRequestDto;
import ru.home.dto.response.BookAuthorResponseDto;
import ru.home.dto.response.BookBorrowedResponseDto;
import ru.home.dto.response.BookDataResponseDto;
import ru.home.exception.DataBaseConnectionException;
import ru.home.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public Long creatBook(BookCreateRequestDto bookCreateRequestDto) {
        String queryCreateBook = "INSERT INTO %s (isbn, title, author) VALUES (?, ?, ?);".formatted(BOOK_TABLE);
        try (PreparedStatement statement = connection.prepareStatement(queryCreateBook, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(FIRST_INDEX, bookCreateRequestDto.isbn());
            statement.setString(SECOND_INDEX, bookCreateRequestDto.title());
            statement.setString(THIRD_INDEX, bookCreateRequestDto.author());
            statement.executeUpdate();
            ResultSet bookId = statement.getGeneratedKeys();
            if(bookId.next()) return bookId.getLong("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0L;
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

    public boolean borrowBook(String isbn, UserIdTypeRequestDto userIdTypeRequestDto) {
        String query = """
                INSERT INTO borrowed_books (user_id, isbn, borrow_day, return_day) 
                SELECT ?, b.isbn, ?, ?
                FROM books b
                WHERE b.isbn = ? AND b.available = true;""";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            statement.setLong(FIRST_INDEX, userIdTypeRequestDto.userId());
            statement.setObject(SECOND_INDEX, LocalDate.now());
            statement.setObject(THIRD_INDEX, LocalDate.now().plusDays(daysAmount(userIdTypeRequestDto)));
            statement.setString(4, isbn);

            return commitChanges(statement, isbn, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean returnBook(String isbn) {
        String query = "DELETE FROM borrowed_books WHERE isbn = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            statement.setString(FIRST_INDEX,isbn);

            return commitChanges(statement, isbn, true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean commitChanges(PreparedStatement statement, String isbn, boolean available) throws SQLException {
        int status = 0;
        if (statement.executeUpdate() != INVALID_ID) {
            status = changeBookAvailability(isbn, available);
        }
        if (status != 0) {
            connection.commit();
        } else {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        return status != 0;
    }

    private int changeBookAvailability(String isbn, boolean available) {
        String query = "UPDATE %s SET available = ? WHERE isbn = ?;".formatted(BOOK_TABLE);
        int status = 0;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(FIRST_INDEX, available);
            statement.setString(SECOND_INDEX, isbn);
            status = statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return status;
    }

    private Long daysAmount(UserIdTypeRequestDto userIdTypeRequestDto) {
        if (userIdTypeRequestDto.type().toString().equalsIgnoreCase("STUDENT")) {
            return 14L;
        } else if (userIdTypeRequestDto.type().toString().equalsIgnoreCase("FACULTY")) {
            return 30L;
        }
        return 7L;
    }

    private static BookDataResponseDto builder(ResultSet resultSet) throws SQLException {
        return new BookDataResponseDto(
                resultSet.getString("isbn"),
                resultSet.getString("author"),
                resultSet.getString("title"));
    }
}
