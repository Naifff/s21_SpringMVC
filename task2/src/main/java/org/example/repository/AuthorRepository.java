package org.example.repository;

import org.example.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
	Optional<Author> findByName(String name);

	// Найти всех авторов, у которых больше указанного количества книг
	@Query("SELECT DISTINCT a FROM Author a LEFT JOIN a.books b GROUP BY a HAVING COUNT(b) > :bookCount")
	List<Author> findAuthorsWithMoreBooksThan(@Param("bookCount") int bookCount);

	// Поиск авторов по части имени (игнорируя регистр)
	List<Author> findByNameContainingIgnoreCase(String namePart);
}
