package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class User {
    private final int id;
    private final String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;

    public User(int id, String email, String login, String name, LocalDate birthday, HashSet<Long> friends ) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = friends;
    }

    public void addFriend(Long id) {
        this.friends.add(id);
    }

    public void deleteFriend(Long id) {
        this.friends.remove(id);
    }
}
