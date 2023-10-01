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
import com.cesar.BookApi.repository.Book_Repository;

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
	
	
	
	
	@GetMapping("/books/by-genre")
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
		
		return ResponseEntity.status(HttpStatus.CREATED).body( book );
	}
	
	
	

	
	@PutMapping( value = "/books/{book_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<?> replace(@PathVariable Long book_id, @RequestBody @Valid Book_DTO replaceBook) {
		
		//if this book already exists..
		if ( bookRepo.findById(book_id).isPresent() ) {
			
			//Set id_book on update data to prevent replacing other book
			replaceBook.setId(book_id);

			//Mapping DTO to entity to replace in BBDD,
			replaceBook = modelMapper.map( bookRepo.save( modelMapper.map( replaceBook, Book.class )), Book_DTO.class ); //and re-mapping to DTO
			
			return ResponseEntity.ok( replaceBook );
		}
		
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	@PatchMapping( value = "/books/{book_id}", consumes = MediaType.APPLICATION_JSON_VALUE) 
	private ResponseEntity<?> update(@PathVariable Long book_id, @RequestBody Map<String, Object> fields) {
		
		Optional<Book> optionalBook = bookRepo.findById(book_id);
		
		//if this book already exists..
		if ( optionalBook.isPresent() ) {
			
			Book book = optionalBook.get();
			
			
			//Remove id to prevent change in entity.
			fields.remove("id");
			
			
			//Set change fields to Entity
			fields.forEach( (k, v) -> {
				
				Field field = ReflectionUtils.findField(Book.class, k);
				
				if ( field != null ) {
									
					field.setAccessible(true);
					ReflectionUtils.setField(field, book, v);
				}
			});
						
			//Mapping to DTO to validate
			Book_DTO dtoValidate = modelMapper.map( book, Book_DTO.class ); 

			
			//Validate
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set< ConstraintViolation<Book_DTO> > violations = validator.validate( dtoValidate );
			
			if ( ! violations.isEmpty() ) {
				
				throw new ConstraintViolationException(violations);
			}
			
			
			//Save change entity to update in BBDD,
			Book_DTO updateBook = modelMapper.map( bookRepo.save( book ), Book_DTO.class ); //and re-mapping to DTO
			
			
			return ResponseEntity.ok( updateBook );
		}
		
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	@DeleteMapping("/books/{book_id}")
	public ResponseEntity<?> delete(@PathVariable Long book_id){
		
		Optional<Book> optionalBook = bookRepo.findById(book_id);
		
		//if this book exists..
		if ( optionalBook.isPresent() ) {
					
			//Delete from BBDD
			bookRepo.deleteById(book_id);
			
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.noContent().build();	
	}
	
	
	
	
	
	//---------------INSTNANCES-------------------

	@Autowired
	private Book_Repository bookRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
}
