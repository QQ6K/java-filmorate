package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;


@Data
public class User {
    private int id;
    private final String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private HashSet<Long> friends;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet();
    }


}
