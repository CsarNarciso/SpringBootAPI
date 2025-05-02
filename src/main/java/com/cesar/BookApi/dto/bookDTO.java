package com.cesar.BookApi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class bookDTO {

	@NotBlank(message = "'name' is required.")
	private String name;
	private String genre;
}