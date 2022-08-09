package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Primary
@Qualifier
@Slf4j
@Component
public class DbUserStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(int id) {
        return null;
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        return null;
    }

    @Override
    public Collection<User> findAll() {
        String sql =
                "SELECT id, name, description, release_date, duration FROM FILMS";
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
}


