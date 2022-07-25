package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
class FilmControllerTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Test
	void getFilmControllerResponseShouldBeOkTest() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders
				.get("/films");
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void createValidFilmResponseShouldBeOkTest() throws Exception {
		Film film = new Film( 1,"Простоквашино",
				"О несчастных и счастливых, " +
						"о добре и зле, о лютой ненависти и святой любви",
				LocalDate.of(1978,6,6), 19, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void createFilmBeforeBirthTest() throws Exception {
		Film film = new Film( 1,"L'arroseur arrosé",
				"Политый поливальщик",
				LocalDate.of(1895,12,27), 2, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void createFilmBirthTest() throws Exception {
		Film film = new Film( 1,"L'arroseur arrosé",
				"Политый поливальщик",
				LocalDate.of(1895,12,28), 2, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void createFilmAfterBirthTest() throws Exception {
		Film film = new Film(1, "L'arroseur arrosé",
				"Политый поливальщик",
				LocalDate.of(1895,12,29), 2, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void createFilmNegativeDurationTest() throws Exception {
		Film film = new Film(1,"Нечто",
				"Группа учёных сообщила о падении летающего объекта",
				LocalDate.of(1982,6,25), -1, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void createFilmZeroDurationTest() throws Exception {
		Film film = new Film(1,"Нечто",
				"Группа учёных сообщила о падении летающего объекта",
				LocalDate.of(1982,6,25), 0, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void createFilmPositiveDurationTest() throws Exception {
		Film film = new Film(1,"Нечто",
				"Группа учёных сообщила о падении летающего объекта",
				LocalDate.of(1982,6,25), 1, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void createValidFilmNameIsEmptyTest() throws Exception {
		Film film = new Film(1,"",
				"Based on his own fascination with spirituality, " +
						"Aykroyd conceived Ghostbusters as a project starring " +
						"himself and John Belushi, in which they would space " +
						"battling supernatural threats.",
				LocalDate.of(1984,6,8), 105, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void createValidFilmNameIsNullTest() throws Exception {
		Film film = new Film(1,null,
				"Based on his own fascination with spirituality, " +
						"Aykroyd conceived Ghostbusters as a project starring " +
						"himself and John Belushi, in which they would space " +
						"battling supernatural threats.",
				LocalDate.of(1984,6,8),105, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void createValidFilmDescription199Test() throws Exception {
		Film film = new Film( 6,"Back to the future",
				"Back to the Future is a 1985 American science" +
						" fiction film. The story follows Marty McFly (Fox)," +
						" a teenager accidentally sent back to 1955 in " +
						"a time-traveling automobile. Directed by Robert Zemeckis.",
				LocalDate.of(1985,7,3), 116, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
		assertEquals(body,result.getResponse().getContentAsString(),
				"Данные Film-post и Film-return не совпадают");
	}

	@Test
	void createValidFilmDescription200Test() throws Exception {
		Film film = new Film( 8,"Back to the future",
				"Back to the Future is a 1985 American science" +
						" fiction film. The story follows Marty McFly (Fox)," +
						" a teenager accidentally sent back to 1955 in " +
						"a time-traveling automobile. Directed by Robert Zemeckis!!",
				LocalDate.of(1985,7,3), 116, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
		assertEquals(body,result.getResponse().getContentAsString(),
				"Данные Film-post и Film-return не совпадают");
	}

	@Test
	void createValidFilmDescription201Test() throws Exception {
		Film film = new Film( 1,"Back to the future",
				"Back to the Future is a 1985 American science" +
						" fiction film. The story follows Marty McFly (Fox)," +
						" a teenager accidentally sent back to 1955 in " +
						"a time-traveling automobile. Directed by Robert Zemeckis!!!",
				LocalDate.of(1985,7,3), 116, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(400, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void createValidFilmPutTest() throws Exception {
		Film film = new Film( 1,"Back",
				"Back!",
				LocalDate.of(1990,1,2), 123, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
	}

	@Test
	void getFilmValidTest() throws Exception {
		Film film = new Film( 7,"Back",
				"Back!",
				LocalDate.of(1990,1,2), 123, 0);
		String body = mapper.writeValueAsString(film);
		RequestBuilder request = MockMvcRequestBuilders.post("/films")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(request);
		request = MockMvcRequestBuilders.get("/films/7")
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
		assertEquals(body,result.getResponse().getContentAsString(),
				"Данные Film-post и Film-return не совпадают");
	}

	@Test
	void getFilmInvalidTest() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/films/-1")
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(404, result.getResponse().getStatus(), "Ожидался код ответа 404");
	}





}
