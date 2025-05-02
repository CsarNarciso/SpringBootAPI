package com.cesar.SpringBootAPI.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponseDTO {
	private Long id;
	private String name;
	private String genre;
}