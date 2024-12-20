package org.example.init;

import jakarta.transaction.Transactional;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.repository.AuthorRepository;
import org.example.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;

	public DataInitializer(AuthorRepository authorRepository, BookRepository bookRepository) {
		this.authorRepository = authorRepository;
		this.bookRepository = bookRepository;
	}

	@Override
	@Transactional
	public void run(String... args) {
		// Создаем авторов
		Author tolstoy = new Author();
		tolstoy.setName("Лев Толстой");
		tolstoy.setBiography("Русский писатель, классик мировой литературы");
		authorRepository.save(tolstoy);

		Author dostoevsky = new Author();
		dostoevsky.setName("Фёдор Достоевский");
		dostoevsky.setBiography("Русский писатель, мыслитель, философ и публицист");
		authorRepository.save(dostoevsky);

		Author pushkin = new Author();
		pushkin.setName("Александр Пушкин");
		pushkin.setBiography("Русский поэт, драматург и прозаик");
		authorRepository.save(pushkin);

		// Создаем книги
		createAndSaveBook("Война и мир", "978-5-17-084879-4", 1869, tolstoy);
		createAndSaveBook("Анна Каренина", "978-5-17-087991-0", 1877, tolstoy);

		createAndSaveBook("Преступление и наказание", "978-5-17-089789-1", 1866, dostoevsky);
		createAndSaveBook("Братья Карамазовы", "978-5-17-088989-2", 1880, dostoevsky);

		createAndSaveBook("Евгений Онегин", "978-5-17-085879-5", 1833, pushkin);
		createAndSaveBook("Капитанская дочка", "978-5-17-086879-6", 1836, pushkin);

		System.out.println("База данных инициализирована тестовыми данными");
	}

	private void createAndSaveBook(String title, String isbn, int year, Author author) {
		Book book = new Book();
		book.setTitle(title);
		book.setIsbn(isbn);
		book.setPublicationYear(year);
		book.setAuthor(author);
		bookRepository.save(book);
	}
}