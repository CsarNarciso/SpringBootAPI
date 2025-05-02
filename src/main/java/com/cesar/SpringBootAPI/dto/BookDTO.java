package com.cesar.SpringBootAPI.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {

	@NotBlank(message = "'name' is required.")
	private String name;
	private String genre;
}