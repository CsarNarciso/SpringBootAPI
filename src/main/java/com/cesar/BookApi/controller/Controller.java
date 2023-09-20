package com.cesar.BookApi.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesar.BookApi.dto.Book_DTO;
import com.cesar.BookApi.entity.Book;
import com.cesar.BookApi.repository.Book_Repository;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/books.api", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {

	
	@GetMapping("/books")
	private ResponseEntity<?> getAll(){
		
		//Mapping list entity books to DTO
		List<Book_DTO> books = bookRepo.findAll().stream()
				.map( bookEntity -> modelMapper.map(bookEntity, Book_DTO.class) )
				.toList();
		
		//If there's books...
		if ( ! books.isEmpty() ) {
			
			return ResponseEntity.ok( books );
		}
		
		return ResponseEntity.noContent().build(); 		
	}

	
	
	
	@GetMapping("/books/{book_id}")
	private ResponseEntity<?> getById(@PathVariable Long book_id) {
		
		//Searching book entity by id
		Optional<Book> bookEntity = bookRepo.findById(book_id);
		
		//If book exist..
		if ( bookEntity.isPresent() ) {
			
			//Mapping entity to DTO
			Book_DTO book = modelMapper.map( bookEntity.get(), Book_DTO.class );
			
			return ResponseEntity.ok( book );
		}
		
		return ResponseEntity.noContent().build();

	}
	
	
	
	
	@GetMapping("/books/by-genree")
	private ResponseEntity<?> getByGenre(@RequestParam("genre") String genre){
		
		//If genres is not empty..
		if ( ! genre.isEmpty() ) {			
			
			//Mapping list entity books to DTO
			List<Book_DTO> books = bookRepo.getByGenre( genre ).stream()
					.map(bookEntity -> modelMapper.map( bookEntity, Book_DTO.class ))
					.toList();
			
			//if there's books of this genre..
			if ( ! books.isEmpty() ) {
				
				return ResponseEntity.ok( books );
			}
		}
		
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	@PostMapping( value = "/books", consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<?> create(@RequestBody @Valid Book_DTO book) {
		
		//Set id in null
		book.setId(null);
			
		//Mapping DTO to entity to register in BBDD,
		book = modelMapper.map( bookRepo.save( modelMapper.map( book, Book.class )), Book_DTO.class ); //and re-mapping to DTO
		
		return ResponseEntity.ok( book );
	}
	
		
	
	
	
	//---------------INSTNANCES-------------------

	@Autowired
	private Book_Repository bookRepo;
	
	@Autowired
	private ModelMapper modelMapper;
}
