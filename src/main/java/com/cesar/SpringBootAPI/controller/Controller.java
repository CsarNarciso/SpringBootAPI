package com.cesar.SpringBootAPI.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cesar.SpringBootAPI.dto.ApplicationResponse;
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

		HttpStatus httpStatus = HttpStatus.CREATED;
		int httpCode = httpStatus.value();

		String message = "Book created successfully";

		Map<String, Object> data = new HashMap<>();
		data.put("book", createdBook);

		ApplicationResponse<?> response = new ApplicationResponse<>(httpCode, message, data);
		return ResponseEntity.status(httpStatus).body(response);
	}

	@GetMapping
	private ResponseEntity<?> getAll(){
		
		List<BookResponseDTO> books = service.getAll();

		HttpStatus httpStatus = HttpStatus.OK;
		int httpCode = httpStatus.value();

		String message = "All existing books fetched";

		Map<String, Object> data = new HashMap<>();
		data.put("books", books);

		ApplicationResponse<?> response = new ApplicationResponse<>(httpCode, message, data);
		return ResponseEntity.status(httpStatus).body(response);
	}

	@GetMapping("/{id}")
	private ResponseEntity<?> getById(@PathVariable Long id) {
		
		BookResponseDTO book = service.getById(id);

		HttpStatus httpStatus = HttpStatus.OK;
		int httpCode = httpStatus.value();

		String message = String.format("Book %s fetched", id);

		Map<String, Object> data = new HashMap<>();
		data.put("book", book);

		ApplicationResponse<?> response = new ApplicationResponse<>(httpCode, message, data);
		return ResponseEntity.status(httpStatus).body(response);
	}

	@GetMapping("/genre/{genre}")
	private ResponseEntity<?> getByGenre(@PathVariable String genre){

		List<BookResponseDTO> books = service.getByGenre(genre);

		HttpStatus httpStatus = HttpStatus.OK;
		int httpCode = httpStatus.value();

		String message = String.format("All existing %s genre books fetched", genre);

		Map<String, Object> data = new HashMap<>();
		data.put("books", books);

		ApplicationResponse<?> response = new ApplicationResponse<>(httpCode, message, data);
		return ResponseEntity.status(httpStatus).body(response);
	}

	
	@PutMapping("/{id}")
	private ResponseEntity<?> replace(@PathVariable Long id, @RequestBody @Valid BookRequestDTO newBookRequest) {

		BookResponseDTO replacedBook = service.replace(id, newBookRequest);

		HttpStatus httpStatus = HttpStatus.OK;
		int httpCode = httpStatus.value();

		String message = String.format("Book %s replaced successfully", id);

		Map<String, Object> data = new HashMap<>();
		data.put("book", replacedBook);

		ApplicationResponse<?> response = new ApplicationResponse<>(httpCode, message, data);
		return ResponseEntity.status(httpStatus).body(response);
	}
	
	
	@PatchMapping("/{id}")
	private ResponseEntity<ApplicationResponse<?>> update(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
		
		BookResponseDTO updatedBook = service.update(id, fields);

		HttpStatus httpStatus = HttpStatus.OK;
		int httpCode = httpStatus.value();

		String message = String.format("Book %s updated successfully", id);

		Map<String, Object> data = new HashMap<>();
		data.put("book", updatedBook);

		ApplicationResponse<?> response = new ApplicationResponse<>(httpCode, message, data);
		return ResponseEntity.status(httpStatus).body(response);
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