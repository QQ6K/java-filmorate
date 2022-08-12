package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface UserStorage {

    User getUser(int id);

    HashMap<Integer, User> getUsers();

    List<User> findAll();

    User create(User user);

    User update(User user);

    boolean checkName(String name);

    boolean checkEmail(String email);

    boolean checkId(int id);

    Set<Long> getFriends(int id);

    void addFriend(int id, int friendId, boolean status);

    void removeFromFriends(int user_id, int friend_id);
}
