package com.cesar.SpringBootAPI.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BookRequestDTO {
    @NotBlank(message = "'name' is required.")
    private String name;
    private String genre;
}
