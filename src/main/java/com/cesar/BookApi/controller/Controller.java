package com.cesar.BookApi.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesar.BookApi.dto.Book_DTO;
import com.cesar.BookApi.entity.Book;
import com.cesar.BookApi.service.BookService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@RestController
@RequestMapping(value = "/books.api", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {

	
	@GetMapping("/books")
	private ResponseEntity<?> getAll(){
		
		//Mapping list entity books to DTO
		List<Book_DTO> books = service.findAll();
		
		//If there's books...
		if ( ! books.isEmpty() ) {
			
			return ResponseEntity.ok( books );
		}
		return ResponseEntity.noContent().build(); 		
	}

	
	
	
	@GetMapping("/books/{book_id}")
	private ResponseEntity<?> getById(@PathVariable Long book_id) {
		
		Book_DTO book = service.findById(book_id);
		
		//If book exist..
		if ( book.isPresent() ) {
			
			return ResponseEntity.ok( book );
		}
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	@GetMapping("/books/by-genre")
	private ResponseEntity<?> getByGenre(@RequestParam("genre") String genre){
		
		List<Book_DTO> books = service.getByGenre(genre);
		
		//if there's books of this genre..
		if ( ! books.isEmpty() ) {
			
			return ResponseEntity.ok( books );
		}
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	@PostMapping( value = "/books", consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<?> create(@RequestBody @Valid Book_DTO book) {
		
		book = service.save(book); 
		return ResponseEntity.status(HttpStatus.CREATED).body( book );
	}
	
	
	

	
	@PutMapping( value = "/books/{book_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<?> replace(@PathVariable Long book_id, @RequestBody @Valid Book_DTO replaceBook) {
		
		//if this book already exists..
		if (service.findById(book_id) != null) {
		
			replaceBook = service.replace(book_id, replaceBook);
			return ResponseEntity.ok( replaceBook );
		}
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	@PatchMapping( value = "/books/{book_id}", consumes = MediaType.APPLICATION_JSON_VALUE) 
	private ResponseEntity<?> update(@PathVariable Long book_id, @RequestBody Map<String, Object> fields) {
		
		//if this book already exists..
		if (service.findById(book_id) != null) {
		
			Book_DTO updatedBook = service.update(book_id, fields);
			return ResponseEntity.ok(updatedBook);
		}
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	@DeleteMapping("/books/{book_id}")
	public ResponseEntity<?> delete(@PathVariable Long book_id) {
		
		Book_DTO book = service.findById(book_id);
		
		//if this book exists..
		if (book != null) {
					
			//Delete from DB
			service.deleteById(book_id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.noContent().build();	
	}
	
	
	
	
	
	//---------------INSTNANCES-------------------

	@Autowired
	private BookService service;
	
	@Autowired
	private ModelMapper modelMapper;
}