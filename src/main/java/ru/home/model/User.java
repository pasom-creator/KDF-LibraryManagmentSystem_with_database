package ru.home.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long userId;
    private String name;
    private String email;
    private final List<String> BORROWED_BOOKS;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.BORROWED_BOOKS = new ArrayList<>();
    }

    public User(Long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.BORROWED_BOOKS = new ArrayList<>();
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getBORROWED_BOOKS() {
        return BORROWED_BOOKS;
    }
}
