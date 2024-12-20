package org.example.dto;

public class AuthorDTO {
	private Long id;
	private String name;
	private String biography;
	private int bookCount;

	// Конструкторы
	public AuthorDTO() {
	}

	public AuthorDTO(Long id, String name, String biography, int bookCount) {
		this.id = id;
		this.name = name;
		this.biography = biography;
		this.bookCount = bookCount;
	}

	// Геттеры и сеттеры
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public int getBookCount() {
		return bookCount;
	}

	public void setBookCount(int bookCount) {
		this.bookCount = bookCount;
	}
}
