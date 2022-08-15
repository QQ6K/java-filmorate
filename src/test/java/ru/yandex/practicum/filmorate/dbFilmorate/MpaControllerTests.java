package ru.yandex.practicum.filmorate.dbFilmorate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.DbGenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.DbMpaStorage;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MpaControllerTests {

    private final DbMpaStorage mpaStorage;
    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();


    @Test
    public void testCreateMpa() throws JsonProcessingException {
        String json = "{\"name\":\"NO-50\"}";
        Mpa mpa = mapper.readValue(json, Mpa.class);
        Mpa out = mpaStorage.create(mpa);
        assertThat(out).hasFieldOrPropertyWithValue("id", 6);
        System.out.println(mapper.writeValueAsString(out));
    }

    @Test
    public void testGetGenre() throws JsonProcessingException {
        testCreateMpa();
        Mpa out = mpaStorage.getMpa(6);
        assertThat(out).hasFieldOrPropertyWithValue("id", 6);
        System.out.println("Вернулось:" + mapper.writeValueAsString(out));
    }

    @Test
    public void testGetAllGenres() throws JsonProcessingException {
        Collection<Mpa> mpas = mpaStorage.findAll();
        String json = "[{\"id\":1,\"name\":\"G\"}," +
                "{\"id\":5,\"name\":\"NC-17\"}," +
                "{\"id\":2,\"name\":\"PG\"}," +
                "{\"id\":3,\"name\":\"PG-13\"}," +
                "{\"id\":4,\"name\":\"R\"}]";
        assertEquals(mapper.writeValueAsString(mpas),json, "Не совпадают жанры с исходными");
        System.out.println(mapper.writeValueAsString(mpas));
    }

    @Test
    public void testUpdateMpa() throws JsonProcessingException {
        testCreateMpa();
        String json = "{\"id\":6,\"name\":\"YES50\"}";
        Mpa mpa = mapper.readValue(json, Mpa.class);
        mpaStorage.update(mpa);
        assertThat(mpaStorage.getMpa(6)).hasFieldOrPropertyWithValue("name", "YES50");
        System.out.println(mapper.writeValueAsString(mpaStorage.getMpa(6)));
    }
}
