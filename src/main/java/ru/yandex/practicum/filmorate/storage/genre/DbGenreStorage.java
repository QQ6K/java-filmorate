package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class DbGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Genre getGenre(int id) {
        Genre genre = new Genre();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet
                ("SELECT * FROM genre_names where id= ?", id);
        if (genreRows.next()) {
            genre.setId(Integer.parseInt(genreRows.getString("id")));
            genre.setName(genreRows.getString("name"));
        }
        return genre;
    }

    public Collection<Genre> findAll() {
        String sql =
                "SELECT * FROM genre_names ORDER BY id ASC";
        return
                jdbcTemplate.query(sql, new ResultSetExtractor<Collection<Genre>>() {
                    @Override
                    public Collection<Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<Genre> genres = new ArrayList<>();
                        while (rs.next()) {
                            genres.add(mapToGenres(rs));
                        }
                        return genres;
                    }
                });
    }

    public Genre create(Genre genre) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("genre_names")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> values = new HashMap<>();
        values.put("name", genre.getName());
        genre.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return genre;
    }

    public Genre update(Genre genre) {
        String sql = "UPDATE genre_names SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, genre.getName(), genre.getId());
        return genre;
    }

    private Genre mapToGenres(ResultSet resultSet) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    }
}
