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
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

    public boolean checkName(String name){
        String sql =
                "SELECT * FROM users WHERE name = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, name);
        if (sqlRowSet.next()) return true;
        return false;
    }

    public boolean checkEmail(String email){
        String sql =
                "SELECT * FROM users WHERE email = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, email);
        if (sqlRowSet.next()) return true;
        return false;
    }

    public boolean checkId(int id){
        String sql =
                "SELECT * FROM users WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (sqlRowSet.next()) return true;
        return false;
    }

    @Override
    public User getUser(int id) {
        User user = new User();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet
                ("SELECT * FROM users where ID= ?", id);
        if (userRows.wasNull()) throw new NoFoundException("Отсутствует пользователь с указанным id = " + id);
        if (userRows.next()) {
            user.setId(Integer.parseInt(userRows.getString("id")));
            user.setEmail(userRows.getString("email"));
            user.setLogin(userRows.getString("login"));
            user.setName(userRows.getString("name"));
            user.setBirthday(userRows.getDate("birthday").toLocalDate());
        }

        String sql =
                "(SELECT friend_id FROM friends  WHERE  user_id = ?) ";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);

        HashSet friends = new HashSet();
        while (sqlRowSet.next()) {
            Long l = sqlRowSet.getLong("friend_id");
            friends.add(l);
        }
        user.setFriends(friends);
        return user;
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        return null;
    }

    @Override
    public List<User> findAll() {
        String sql =
                "SELECT * FROM users";
        return
                jdbcTemplate.query(sql, new ResultSetExtractor<List<User>>() {
                    @Override
                    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<User> users = new ArrayList<>();
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
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
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
    public User update(User user) {
        String sql =
                "UPDATE users SET email = ?, login = ?, name = ? , birthday = ?" +
                        "WHERE id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public Set<Long> getFriends(int id) {
        String sql =
                "(SELECT friend_id ID FROM friend  WHERE  user1_id = ?) ";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        Set<Long> friends = new HashSet<>();
        while (sqlRowSet.next()) {
            friends.add(sqlRowSet.getLong("id"));
        }
        return friends;
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
        String sql = "SELECT * FROM friends WHERE user_id = ? AND friend_id = ? AND  staatus = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, filterId1, filterId2, filterConfirmed);
        return rows.next();
    }

    public void updateFriends(Long id1, Long id2, boolean confirmed,  Long filterId1, Long filterId2) {
        String sql =
                "UPDATE friends SET user_id = ?, friend_id = ?, status = ? " +
                        "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id1, id2, confirmed, filterId1, filterId2);
    }

    @Override
    public void addFriend(int id, int friendId, boolean status) {
        String sql = "INSERT INTO friends (user_id, friend_id, status) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, id, friendId, status);
        if (status) {
            sql =
                    "UPDATE friends SET status = ? " +
                            "WHERE friend_id = ? AND user_id = ?";
            jdbcTemplate.update(sql,  true, friendId, id);
        }
    }

    @Override
    public void removeFromFriends(int user_id, int friend_id) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, user_id, friend_id);
    }


}


