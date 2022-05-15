package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;


@Data
public class User {
    private final int id;
    private final String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
