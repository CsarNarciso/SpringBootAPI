package com.cesar.SpringBootAPI.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesar.SpringBootAPI.dto.BookDTO;
import com.cesar.SpringBootAPI.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class Controller {

	@GetMapping
	private ResponseEntity<?> getAll(){
		
		List<BookDTO> books = service.getAll();
		return ResponseEntity.ok(books);
	}

	
	@GetMapping("/{id}")
	private ResponseEntity<?> getById(@PathVariable Long id) {
		
		BookDTO book = service.getById(id);
		return ResponseEntity.ok(book);
	}

	
	@GetMapping("/books/{genre}")
	private ResponseEntity<?> getByGenre(@PathVariable String genre){
		
		List<BookDTO> books = service.getByGenre(genre);
		return ResponseEntity.ok(books);
	}

	
	@PostMapping
	private ResponseEntity<?> create(@RequestBody @Valid BookDTO createRequest) {
		BookDTO createdBook = service.create(createRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
	}

	
	@PutMapping("/{id}")
	private ResponseEntity<?> replace(@PathVariable Long id, @RequestBody @Valid BookDTO newBookRequest) {

		BookDTO replacedBook = service.replace(id, newBookRequest);
		return ResponseEntity.ok(replacedBook);
	}
	
	
	@PatchMapping("/{id}")
	private ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
		
		BookDTO updatedBook = service.update(id, fields);
		return ResponseEntity.ok(updatedBook);
	}


	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
	
	
	
	public Controller(BookService service){
		this.service = service;
	}
	
	private final BookService service;
}