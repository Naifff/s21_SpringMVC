package org.example;

import org.example.dto.BookDTO;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.example.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
	@Mock
	private BookRepository bookRepository;

	@Mock
	private AuthorRepository authorRepository;

	@InjectMocks
	private BookService bookService;

	private Book testBook;
	private Author testAuthor;
	private BookDTO testBookDTO;

	@BeforeEach
	void setUp() {
		// Подготавливаем тестовые данные
		testAuthor = new Author();
		testAuthor.setId(1L);
		testAuthor.setName("Test Author");

		testBook = new Book();
		testBook.setId(1L);
		testBook.setTitle("Test Book");
		testBook.setIsbn("1234567890");
		testBook.setPublicationYear(2024);
		testBook.setAuthor(testAuthor);

		testBookDTO = new BookDTO();
		testBookDTO.setTitle("Test Book");
		testBookDTO.setIsbn("1234567890");
		testBookDTO.setPublicationYear(2024);
		testBookDTO.setAuthorId(1L);
	}

	@Test
	void whenCreateBook_thenBookShouldBeSaved() {
		// Подготовка
		when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
		when(bookRepository.save(any(Book.class))).thenReturn(testBook);

		// Выполнение
		BookDTO result = bookService.createBook(testBookDTO);

		// Проверка
		assertNotNull(result);
		assertEquals(testBookDTO.getTitle(), result.getTitle());
		assertEquals(testBookDTO.getIsbn(), result.getIsbn());
		verify(bookRepository).save(any(Book.class));
	}

	@Test
	void whenGetBook_thenReturnBook() {
		// Подготовка
		when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

		// Выполнение
		BookDTO result = bookService.getBook(1L);

		// Проверка
		assertNotNull(result);
		assertEquals(testBook.getTitle(), result.getTitle());
		assertEquals(testBook.getIsbn(), result.getIsbn());
	}

	@Test
	void whenGetAllBooks_thenReturnPageOfBooks() {
		// Подготовка
		Pageable pageable = PageRequest.of(0, 10);
		Page<Book> bookPage = new PageImpl<>(Arrays.asList(testBook));

		when(bookRepository.findAll(pageable)).thenReturn(bookPage);

		// Выполнение
		Page<BookDTO> result = bookService.getAllBooks(pageable);

		// Проверка
		assertNotNull(result);
		assertEquals(1, result.getContent().size());
		assertEquals(testBook.getTitle(), result.getContent().get(0).getTitle());
	}

	@Test
	void whenGetNonExistingBook_thenThrowException() {
		// Подготовка
		when(bookRepository.findById(99L)).thenReturn(Optional.empty());

		// Проверка
		assertThrows(ResourceNotFoundException.class, () ->
				bookService.getBook(99L)
		);
	}

	@Test
	void whenUpdateBook_thenReturnUpdatedBook() {
		// Настраиваем только необходимые заглушки
		when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
		when(bookRepository.save(any(Book.class))).thenReturn(testBook);

		testBookDTO.setTitle("Updated Title");
		BookDTO result = bookService.updateBook(1L, testBookDTO);

		assertNotNull(result);
		assertEquals("Updated Title", result.getTitle());
		verify(bookRepository).save(any(Book.class));
	}

	@Test
	void whenUpdateBookWithNewAuthor_thenReturnUpdatedBookWithNewAuthor() {
		// Создаем нового автора
		Author newAuthor = new Author();
		newAuthor.setId(2L);
		newAuthor.setName("Новый автор");

		// Настраиваем все необходимые заглушки
		when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
		when(authorRepository.findById(2L)).thenReturn(Optional.of(newAuthor));
		when(bookRepository.save(any(Book.class))).thenReturn(testBook);

		// Меняем автора в DTO
		testBookDTO.setAuthorId(2L);
		BookDTO result = bookService.updateBook(1L, testBookDTO);

		assertNotNull(result);
		assertEquals(2L, result.getAuthorId());
		verify(bookRepository).save(any(Book.class));
		verify(authorRepository).findById(2L);
	}
}