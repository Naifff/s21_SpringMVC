package org.example.service;

import org.example.exception.ResourceNotFoundException;
import org.example.dto.BookDTO;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	private final BookRepository bookRepository;
	private final AuthorRepository authorRepository;

	@Autowired
	public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
		this.bookRepository = bookRepository;
		this.authorRepository = authorRepository;
	}

	public Page<BookDTO> findBooksByPublicationYear(Integer year, Pageable pageable) {
		return bookRepository.findByPublicationYear(year, pageable)
				.map(this::convertToDTO);
	}

	// Добавляем метод для поиска книг по автору с пагинацией
	public Page<BookDTO> findBooksByAuthor(Long authorId, Pageable pageable) {
		return bookRepository.findByAuthorId(authorId, pageable)
				.map(this::convertToDTO);
	}

	public BookDTO createBook(BookDTO bookDTO) {
		Author author = authorRepository.findById(bookDTO.getAuthorId())
				.orElseThrow(() -> new ResourceNotFoundException("Author not found"));

		Book book = new Book();
		book.setTitle(bookDTO.getTitle());
		book.setIsbn(bookDTO.getIsbn());
		book.setPublicationYear(bookDTO.getPublicationYear());
		book.setAuthor(author);

		Book savedBook = bookRepository.save(book);
		return convertToDTO(savedBook);
	}

	public BookDTO getBook(Long id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found"));
		return convertToDTO(book);
	}

	public Page<BookDTO> getAllBooks(Pageable pageable) {
		// findAll с Pageable автоматически возвращает Page<Book>
		return bookRepository.findAll(pageable)
				.map(this::convertToDTO);  // преобразуем каждую книгу в DTO
	}

	public BookDTO updateBook(Long id, BookDTO bookDTO) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found"));

		book.setTitle(bookDTO.getTitle());
		book.setIsbn(bookDTO.getIsbn());
		book.setPublicationYear(bookDTO.getPublicationYear());

		if (!book.getAuthor().getId().equals(bookDTO.getAuthorId())) {
			Author newAuthor = authorRepository.findById(bookDTO.getAuthorId())
					.orElseThrow(() -> new ResourceNotFoundException("Author not found"));
			book.setAuthor(newAuthor);
		}

		Book updatedBook = bookRepository.save(book);
		return convertToDTO(updatedBook);
	}

	public void deleteBook(Long id) {
		if (!bookRepository.existsById(id)) {
			throw new ResourceNotFoundException("Book not found");
		}
		bookRepository.deleteById(id);
	}

	private BookDTO convertToDTO(Book book) {
		BookDTO dto = new BookDTO();
		dto.setId(book.getId());
		dto.setTitle(book.getTitle());
		dto.setIsbn(book.getIsbn());
		dto.setPublicationYear(book.getPublicationYear());
		dto.setAuthorId(book.getAuthor().getId());
		return dto;
	}
}