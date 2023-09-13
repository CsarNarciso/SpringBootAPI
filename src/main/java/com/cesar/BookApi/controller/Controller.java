package com.cesar.BookApi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesar.BookApi.dto.Book_DTO;
import com.cesar.BookApi.entity.Book;
import com.cesar.BookApi.repository.Book_Repository;

@RestController
@RequestMapping(value = "/books.api", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {

	@GetMapping("/books/gender/{gender}")
	private ResponseEntity<?> getByGender(@PathVariable String gender){
		
		List<Book_DTO> books = bookRepo.getAllByGender(gender).stream()
				.map(bookPost -> modelMapper.map(bookPost, Book_DTO.class))
				.collect(Collectors.toList());
		
		if ( books.isEmpty() ) {
			
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok( books );
	}
	
	
	
	
	
	@Autowired
	private Book_Repository bookRepo;
	
	@Autowired
	private ModelMapper modelMapper;
}
