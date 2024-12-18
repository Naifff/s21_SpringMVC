package com.example.jsonview.controller;

import org.example.jsonview.controller.UserController;
import org.example.jsonview.model.User;
import org.example.jsonview.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void getAllUsers_ShouldReturnUsersList() throws Exception {
		User user = new User();
		user.setId(1L);
		user.setName("Test User");
		user.setEmail("test@example.com");

		when(userService.getAllUsers()).thenReturn(Arrays.asList(user));

		mockMvc.perform(get("/api/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("Test User"))
				.andExpect(jsonPath("$[0].email").value("test@example.com"))
				.andExpect(jsonPath("$[0].orders").doesNotExist());
	}

	@Test
	public void createUser_WithValidData_ShouldReturnCreatedUser() throws Exception {
		User user = new User();
		user.setName("Test User");
		user.setEmail("test@example.com");

		User createdUser = new User();
		createdUser.setId(1L);
		createdUser.setName("Test User");
		createdUser.setEmail("test@example.com");

		when(userService.createUser(any(User.class))).thenReturn(createdUser);

		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Test User"))
				.andExpect(jsonPath("$.email").value("test@example.com"));
	}
}