package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
class FilmorateApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Test
	void getFromUserControllerResponseShouldBeOk() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/users");
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	void createValidUserResponseShouldBeOk() throws Exception {
		User user = new User(1, "test@test.ru", "test", "Stephan", LocalDate.of(2000,11,11));
		String body = mapper.writeValueAsString(user);
		RequestBuilder request = MockMvcRequestBuilders.post("/users")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andReturn();
		assertEquals(200, result.getResponse().getStatus(), "Ожидался код ответа 200");
		assertEquals(body,result.getResponse().getContentAsString(), "Данные User-post и User-return не совпадают");
	}


}
