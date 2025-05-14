package com.cesar.SpringBootAPI.controller;

import java.util.List;
import java.util.Map;

import com.cesar.SpringBootAPI.dto.ApplicationResponse;
import com.cesar.SpringBootAPI.dto.BookRequestDTO;
import com.cesar.SpringBootAPI.util.ApplicationResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.cesar.SpringBootAPI.dto.BookResponseDTO;
import com.cesar.SpringBootAPI.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class Controller<T> {

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	private ApplicationResponse<Map<String, Object>> create(@RequestBody @Valid BookRequestDTO createRequest) {
		BookResponseDTO createdBook = service.create(createRequest);
		return ApplicationResponseUtils.buildResponse(201, "Book created successfully", "book", createdBook);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	private ApplicationResponse<Map<String, Object>> getAll(){
		List<BookResponseDTO> books = service.getAll();
		return ApplicationResponseUtils.buildResponse(200, "All existing books fetched", "books", books);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	private ApplicationResponse<Map<String, Object>> getById(@PathVariable Long id) {
		BookResponseDTO book = service.getById(id);
		return ApplicationResponseUtils.buildResponse(200, String.format("Book %s fetched", id), "book", book);
	}

	@GetMapping("/genre/{genre}")
	@ResponseStatus(HttpStatus.OK)
	private ApplicationResponse<Map<String, Object>> getByGenre(@PathVariable String genre){
		List<BookResponseDTO> books = service.getByGenre(genre);
		return ApplicationResponseUtils.buildResponse(200, String.format("All existing %s genre books fetched", genre), "books", books);
	}

	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	private ApplicationResponse<Map<String, Object>> replace(@PathVariable Long id, @RequestBody @Valid BookRequestDTO newBookRequest) {
		BookResponseDTO replacedBook = service.replace(id, newBookRequest);
		return ApplicationResponseUtils.buildResponse(200, String.format("Book %s replaced successfully", id), "book", replacedBook);
	}
	
	
	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	private ApplicationResponse<Map<String, Object>> update(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
		BookResponseDTO updatedBook = service.update(id, fields);
		return ApplicationResponseUtils.buildResponse(200, String.format("Book %s updated successfully", id), "book", updatedBook);
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