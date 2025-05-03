package com.cesar.SpringBootAPI.controller;

import java.util.List;
import java.util.Map;

import com.cesar.SpringBootAPI.dto.BookRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cesar.SpringBootAPI.dto.BookResponseDTO;
import com.cesar.SpringBootAPI.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class Controller {

	@PostMapping
	private ResponseEntity<?> create(@RequestBody @Valid BookRequestDTO createRequest) {
		BookResponseDTO createdBook = service.create(createRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
	}

	@GetMapping
	private ResponseEntity<?> getAll(){
		
		List<BookResponseDTO> books = service.getAll();
		return ResponseEntity.ok(books);
	}

	@GetMapping("/{id}")
	private ResponseEntity<?> getById(@PathVariable Long id) {
		
		BookResponseDTO book = service.getById(id);
		return ResponseEntity.ok(book);
	}

	@GetMapping("/genre/{genre}")
	private ResponseEntity<?> getByGenre(@PathVariable String genre){

		List<BookResponseDTO> books = service.getByGenre(genre);
		return ResponseEntity.ok(books);
	}

	
	@PutMapping("/{id}")
	private ResponseEntity<?> replace(@PathVariable Long id, @RequestBody @Valid BookRequestDTO newBookRequest) {

		BookResponseDTO replacedBook = service.replace(id, newBookRequest);
		return ResponseEntity.ok(replacedBook);
	}
	
	
	@PatchMapping("/{id}")
	private ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
		
		BookResponseDTO updatedBook = service.update(id, fields);
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