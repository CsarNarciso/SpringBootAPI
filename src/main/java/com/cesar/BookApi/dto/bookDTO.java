package com.cesar.BookApi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class bookDTO {

	@NotBlank(message = "'name' is required.")
	private String name;
	private String genre;
}