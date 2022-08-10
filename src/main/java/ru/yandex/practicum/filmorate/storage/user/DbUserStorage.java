package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoFoundException;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Primary
@Qualifier
@Slf4j
@Component
public class DbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(int id) {
        User user = new User();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet
                ("SELECT * FROM users where ID= ?");
        if (userRows.wasNull()) throw new NoFoundException("Отсутствует пользователь с указанным id = " + id);
        if (userRows.next()) {
            user.setId(Integer.parseInt(userRows.getString("id")));
            user.setEmail(userRows.getString("email"));
            user.setLogin(userRows.getString("login"));
            user.setName(userRows.getString("name"));
            user.setBirthday(userRows.getDate("birthday").toLocalDate());
        }
        return user;
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        return null;
    }

    @Override
    public Collection<User> findAll() {
        String sql =
                "SELECT * FROM users";
        return
                jdbcTemplate.query(sql, new ResultSetExtractor<Collection<User>>() {
                    @Override
                    public Collection<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        Collection<User> users = new ArrayList<>();
                        while(rs.next()){
                            users.add(MapUser(rs));
                        }
                        return users;
                    }
                });
    }

    private User MapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        return user;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("ID");
        Map<String, Object> values = new HashMap<>();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("birthday",user.getBirthday());
        user.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return user;
    }

    @Override
    public User put(User user) {
        String sql =
                "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ? , BIRTHDAY = ?" +
                        "WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getLogin(), user.getEmail(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    public boolean containsEmail(String email) {
        String sql = "SELECT * FROM user WHERE EMAIL = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, email);
        return filmRows.next();
    }

   /* public void addFriend(int id, int friendId) {
        User user = this.getUser(id);
        if (user.getFriends().contains((long)friendId)) {
            throw new NoFoundException("Пользователь добавлен в друзья");
        }
        User friend = this.getUser(friendId);
        user.getFriends().add((long)friendId);
        if (friend.getFriends().contains(id)) {
            String sql =
                    "UPDATE friends SET status=true WHERE (user1_id = ?, user2_id = ?)";
            jdbcTemplate.update(sql,id,friendId);
            jdbcTemplate.update(sql,friendId,id);
        }
    }*/

    public boolean containsFriendship(Long filterId1, Long filterId2, Boolean filterConfirmed) {
        String sql = "SELECT * FROM friends WHERE user1_id = ? AND user2_id = ? AND  staatus = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, filterId1, filterId2, filterConfirmed);
        return rows.next();
    }

    public void updateFriendship(Long id1, Long id2, boolean confirmed,  Long filterId1, Long filterId2) {
        String sql =
                "UPDATE friends SET user1_id = ?, user2_id = ?, status = ? " +
                        "WHERE user1_id = ? AND user2_id = ?";
        jdbcTemplate.update(sql, id1, id2, confirmed, filterId1, filterId2);
    }


    public void insertFriendship(Long id, Long friendId) {
        String sql = "INSERT INTO FRIENDSHIP (user1_id, user2_id, status) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, id, friendId, false);
    }


    public void removeFriendship(Long filterId1, Long filterId2) {
        String sql = "DELETE FROM FRIENDSHIP WHERE user2_id = ? AND user1_id = ?";
        jdbcTemplate.update(sql, filterId1, filterId2);
    }


}


