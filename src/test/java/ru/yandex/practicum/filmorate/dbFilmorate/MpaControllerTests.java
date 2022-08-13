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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testCreateMpa() throws JsonProcessingException {
        String json = "\"{\"name\":\"NO-50\"}\"";
        Mpa mpa = mapper.readValue(json, Mpa.class);
        assertThat(mpaStorage.create(mpa)).hasFieldOrPropertyWithValue("id", 6);
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testGetGenre() throws JsonProcessingException {
        testCreateMpa();
        assertThat(mpaStorage.getMpa(6)).hasFieldOrPropertyWithValue("id", 6);
        System.out.println(mapper.writeValueAsString(mpaStorage.getMpa(7)));
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testGetAllGenres() throws JsonProcessingException {
        Collection<Mpa> mpas = mpaStorage.findAll();
        String json = "[{\"id\":1,\"name\":\"Комедия\"}," +
                "{\"id\":2,\"name\":\"Драма\"}," +
                "{\"id\":3,\"name\":\"Мультфильм\"}," +
                "{\"id\":4,\"name\":\"Триллер\"}," +
                "{\"id\":5,\"name\":\"Документальный\"}," +
                "{\"id\":6,\"name\":\"Боевик\"}]";
        assertEquals(mapper.writeValueAsString(mpas),json, "Не совпадают жанры с исходными");
        System.out.println(mapper.writeValueAsString(mpas));
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testUpdateGenre() throws JsonProcessingException {
        testCreateMpa();
        System.out.println(mapper.writeValueAsString(mpaStorage.getMpa(7)));
        String json = "{\"id\":7,\"name\":\"Трагикомедия\"}";
        Mpa mpa = mapper.readValue(json, Mpa.class);
        mpaStorage.update(mpa);
        assertThat(mpaStorage.getMpa(7)).hasFieldOrPropertyWithValue("name", "Трагикомедия");
        System.out.println(mapper.writeValueAsString(mpaStorage.getMpa(7)));
    }
}
