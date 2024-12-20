package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.BookDTO;
import org.example.service.BookService;
import org.example.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/books")
public class BookController {
	private final BookService bookService;

	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping
	public ResponseEntity<Page<BookDTO>> getAllBooks(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "title") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
				Sort.Direction.DESC : Sort.Direction.ASC;
		Sort sort = Sort.by(sortDirection, sortBy);

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<BookDTO> result = bookService.getAllBooks(pageable);

		return ResponseEntity.ok(result);
	}

	@GetMapping("/search")
	public ResponseEntity<PageResponse<BookDTO>> searchBooks(
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Long authorId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<BookDTO> bookPage;

		if (year != null) {
			bookPage = bookService.findBooksByPublicationYear(year, pageable);
		} else if (authorId != null) {
			bookPage = bookService.findBooksByAuthor(authorId, pageable);
		} else {
			bookPage = bookService.getAllBooks(pageable);
		}

		PageResponse<BookDTO> response = new PageResponse<>(
				bookPage.getContent(),
				bookPage.getNumber(),
				bookPage.getSize(),
				bookPage.getTotalElements(),
				bookPage.getTotalPages(),
				bookPage.isFirst(),
				bookPage.isLast()
		);

		return ResponseEntity.ok(response);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO createBook(@RequestBody @Valid BookDTO bookDTO) {
		return bookService.createBook(bookDTO);
	}

	@GetMapping("/{id}")
	public BookDTO getBook(@PathVariable Long id) {
		return bookService.getBook(id);
	}

	/*@GetMapping
	public List<BookDTO> getAllBooks() {
		return bookService.getAllBooks();
	}*/

	@PutMapping("/{id}")
	public BookDTO updateBook(@PathVariable Long id, @RequestBody @Valid BookDTO bookDTO) {
		return bookService.updateBook(id, bookDTO);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBook(@PathVariable Long id) {
		bookService.deleteBook(id);
	}
}