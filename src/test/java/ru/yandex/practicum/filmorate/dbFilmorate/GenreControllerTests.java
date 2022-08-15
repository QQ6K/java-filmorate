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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.genre.DbGenreStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenreControllerTests {

    private final DbGenreStorage genreStorage;
    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();


    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testCreateGenre() throws JsonProcessingException {
        String json = "{\"name\":\"Трагедия\"}";
        Genre genre = mapper.readValue(json, Genre.class);
        assertThat(genreStorage.create(genre)).hasFieldOrPropertyWithValue("id", 7);
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testGetGenre() throws JsonProcessingException {
        testCreateGenre();
        assertThat(genreStorage.getGenre(7)).hasFieldOrPropertyWithValue("id", 7);
        System.out.println(mapper.writeValueAsString(genreStorage.getGenre(7)));
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testGetAllGenres() throws JsonProcessingException {
        Collection<Genre> genres = genreStorage.findAll();
        String json = "[{\"id\":1,\"name\":\"Комедия\"}," +
                "{\"id\":2,\"name\":\"Драма\"}," +
                "{\"id\":3,\"name\":\"Мультфильм\"}," +
                "{\"id\":4,\"name\":\"Триллер\"}," +
                "{\"id\":5,\"name\":\"Документальный\"}," +
                "{\"id\":6,\"name\":\"Боевик\"}]";
        assertEquals(mapper.writeValueAsString(genres),json, "Не совпадают жанры с исходными");
        System.out.println(mapper.writeValueAsString(genres));
    }

    @Test
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    public void testUpdateGenre() throws JsonProcessingException {
        testCreateGenre();
        System.out.println(mapper.writeValueAsString(genreStorage.getGenre(7)));
        String json = "{\"id\":7,\"name\":\"Трагикомедия\"}";
        Genre genre = mapper.readValue(json, Genre.class);
        genreStorage.update(genre);
        assertThat(genreStorage.getGenre(7)).hasFieldOrPropertyWithValue("name", "Трагикомедия");
        System.out.println(mapper.writeValueAsString(genreStorage.getGenre(7)));
    }



}
