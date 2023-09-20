package com.cesar.BookApi.dto;

import jakarta.validation.constraints.NotBlank;

public class Book_DTO {

	private Long id;

	@NotBlank(message = "'name' is required.")
	private String name;
	
	@NotBlank(message = "'genre' is required.")
	private String genre;

	
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

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

}
