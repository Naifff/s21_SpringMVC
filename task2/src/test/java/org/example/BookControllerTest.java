package org.example;

import org.example.controller.BookController;
import org.example.dto.BookDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@Autowired
	private ObjectMapper objectMapper;

	private BookDTO testBookDTO;

	@BeforeEach
	void setUp() {
		testBookDTO = new BookDTO();
		testBookDTO.setId(1L);
		testBookDTO.setTitle("Тестовая книга");
		testBookDTO.setIsbn("1234567890");
		testBookDTO.setPublicationYear(2024);
		testBookDTO.setAuthorId(1L);
	}

	@Test
	void whenCreateBook_thenReturnCreatedBook() throws Exception {
		when(bookService.createBook(any(BookDTO.class))).thenReturn(testBookDTO);

		mockMvc.perform(post("/api/books")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testBookDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value(testBookDTO.getTitle()))
				.andExpect(jsonPath("$.isbn").value(testBookDTO.getIsbn()));
	}

	@Test
	void whenGetBook_thenReturnBook() throws Exception {
		when(bookService.getBook(1L)).thenReturn(testBookDTO);

		mockMvc.perform(get("/api/books/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value(testBookDTO.getTitle()))
				.andExpect(jsonPath("$.isbn").value(testBookDTO.getIsbn()));
	}

	@Test
	void whenGetAllBooks_thenReturnPageOfBooks() throws Exception {
		Page<BookDTO> page = new PageImpl<>(Arrays.asList(testBookDTO));
		when(bookService.getAllBooks(any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/api/books")
						.param("page", "0")
						.param("size", "10")
						.param("sort", "title,desc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].title").value(testBookDTO.getTitle()))
				.andExpect(jsonPath("$.totalElements").value(1));
	}

	@Test
	void whenUpdateBook_thenReturnUpdatedBook() throws Exception {
		when(bookService.updateBook(eq(1L), any(BookDTO.class))).thenReturn(testBookDTO);

		mockMvc.perform(put("/api/books/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(testBookDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value(testBookDTO.getTitle()));
	}

	@Test
	void whenDeleteBook_thenReturn204() throws Exception {
		mockMvc.perform(delete("/api/books/{id}", 1L))
				.andExpect(status().isNoContent());
	}

	@Test
	void whenGetNonExistingBook_thenReturn404() throws Exception {
		when(bookService.getBook(99L))
				.thenThrow(new ResourceNotFoundException("Book not found"));

		mockMvc.perform(get("/api/books/{id}", 99L))
				.andExpect(status().isNotFound());
	}
}