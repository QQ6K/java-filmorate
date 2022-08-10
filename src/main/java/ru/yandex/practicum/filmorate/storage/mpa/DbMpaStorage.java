package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.interfaces.MpaStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DbMpaStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpa(int id) {
        Mpa mpa = new Mpa();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet
                ("SELECT * FROM genre_names where id= ?");
        if (genreRows.next()) {
            mpa.setId(Integer.parseInt(genreRows.getString("id")));
            mpa.setName(genreRows.getString("name"));
        }
        return mpa;
    }


    @Override
    public List<Mpa> findAll() {
        String sql = "SELECT * FROM mpa_names";
        return jdbcTemplate.query(sql, this::mapToRating);
    }

    @Override
    public Mpa create(Mpa mpa) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("mpa_names")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> values = new HashMap<>();
        values.put("name", mpa.getName());
        mpa.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return mpa;
    }

    @Override
    public Mpa update(Mpa mpa) {
        String sql = "UPDATE mpa_names SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, mpa.getName(), mpa.getId());
        return mpa;
    }

    private Mpa mapToRating(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa rating = new Mpa();
        rating.setId(resultSet.getInt("mpa_names"));
        rating.setName(resultSet.getString("name"));
        return rating;
    }
}
