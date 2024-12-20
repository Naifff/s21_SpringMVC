package org.example.repository;

import org.example.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByAuthorId(Long authorId);

	Optional<Book> findByIsbn(String isbn);

	Page<Book> findByPublicationYear(Integer year, Pageable pageable);

	Page<Book> findByAuthorId(Long authorId, Pageable pageable);

	@Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
	Page<Book> findByTitleContaining(@Param("title") String title, Pageable pageable);
}