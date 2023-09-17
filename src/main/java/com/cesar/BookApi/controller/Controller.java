package com.cesar.BookApi.controller;

import java.util.Arrays;
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
	
	
	
	
	@GetMapping("/books/by-genres")
	private ResponseEntity<?> getByGenres(@RequestParam(name = "genres") List<String> genres){
		
		//If genres is not empty..
		if ( ! genres.isEmpty() ) {			
			
			//Mapping list entity books to DTO
			List<Book_DTO> books = bookRepo.getAllByGenres( genres ).stream()
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
	private ResponseEntity<?> create(@RequestBody Book_DTO book) {
		
		//If book contains data..
		if ( book.getName() != null && book.getGenres() != null ) {
			
			if ( ! book.getName().isBlank() && ! book.getGenres().isEmpty() ) {
			
				//Verify genres
				List<String> bookGenres = book.getGenres();
				
				boolean supportedGenres = bookGenres.stream()
						.allMatch( bG -> apiGenres.stream() 
								.anyMatch( aG -> aG.equals( bG ) 
										)
								);
			
				//And if they are supported..
				if ( supportedGenres ) {
						
					//Mapping book to entity to register in BBDD,
					book = modelMapper.map( bookRepo.save( modelMapper.map( book, Book.class )), Book_DTO.class ); //and re-mapping to DTO
					
					return ResponseEntity.ok( book );
				}
			}
		}
		
		return ResponseEntity.badRequest().build();
	}

	
	
	
	
	
	
	
	//---------------INSTNANCES-------------------
	
	private List<String> apiGenres = Arrays.asList("horror", "science-fiction", "fantasy"); //Supported genres for Api
	
	@Autowired
	private Book_Repository bookRepo;
	
	@Autowired
	private ModelMapper modelMapper;
}
