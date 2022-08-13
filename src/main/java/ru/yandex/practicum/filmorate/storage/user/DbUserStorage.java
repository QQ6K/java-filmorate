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
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.utilities.QueriesStaticStrings.*;

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
        User user = jdbcTemplate.query(getGetAllColumnsUserById, this::mapToUser, id).get(0);
        HashSet friends = new HashSet(jdbcTemplate.query(getUserFriends,this::mapToFriends,id));
        user.setFriends(friends);
        return user;
    }

    @Override
    public List<User> findAll() {
        List<Integer> userIds = jdbcTemplate.query(getUserIds, this::mapId);
        List<User> users = new ArrayList<>(Collections.emptySet());
        for (Integer id : userIds) {
            users.add(getUser(id));
        }
        return users;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
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
        jdbcTemplate.update(updateUserInfo,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getUser(user.getId());
    }

    @Override
    public Set<Long> getFriends(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getUserFriends, id);
        Set<Long> friends = new HashSet<>();
        while (sqlRowSet.next()) {
            friends.add(sqlRowSet.getLong("id"));
        }
        return friends;
    }

    public boolean containsFriendship(Long filterId1, Long filterId2, Boolean filterConfirmed) {
        SqlRowSet rows = jdbcTemplate.queryForRowSet(checkFriendship, filterId1, filterId2, filterConfirmed);
        return rows.next();
    }

    public void updateFriends(Long id1, Long id2, boolean confirmed,  Long filterId1, Long filterId2) {
        jdbcTemplate.update(updateFriendship, id1, id2, confirmed, filterId1, filterId2);
    }

    @Override
    public void addFriend(int id, int friendId, boolean status) {
        jdbcTemplate.update(insertFriend, id, friendId, status);
        if (status) {
            jdbcTemplate.update(updateFriend,  true);
        }
    }

    @Override
    public void removeFromFriends(int user_id, int friend_id) {
        jdbcTemplate.update(deleteFriend, user_id, friend_id);
    }

    public boolean checkName(String name){
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(checkUserNameExist, name);
        if (sqlRowSet.next()) return true;
        return false;
    }

    public boolean checkEmail(String email){
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(checkUserEmailExist, email);
        if (sqlRowSet.next()) return true;
        return false;
    }

    public boolean checkId(int id){
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(getAllColumnsUserById, id);
        if (sqlRowSet.next()) return true;
        return false;
    }

    private User mapToUser(ResultSet resultSet, int id) throws SQLException {
        User user = new User();
        user.setId(Integer.parseInt(resultSet.getString("id")));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        return user;
    }

    private Long mapToFriends(ResultSet resultSet, int id) throws SQLException {
        return resultSet.getLong("friend_id");
    }

    private int mapId(ResultSet resultSet, int id) throws SQLException {
        return resultSet.getInt("id");
    }
}


