package com.cesar.BookApi.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	
	@GetMapping("/books/")
	private ResponseEntity<?> getAll(){
		
		//Mapear list entity books a dto
		List<Book_DTO> books = bookRepo.findAll().stream()
				.map( bookEntity -> modelMapper.map(bookEntity, Book_DTO.class) )
				.collect(Collectors.toList());
		
		//Si hay libros...
		if ( ! books.isEmpty() ) {
			
			return ResponseEntity.ok( books );
		}
		
		return ResponseEntity.noContent().build(); 		
	}

	
	
	
	@GetMapping("/books/{book_id}")
	private ResponseEntity<?> getById(@PathVariable Long book_id){
		
		//Buscar book entity por id
		Optional<Book> bookEntity = bookRepo.findById(book_id);
		
		//Si el libro existe..
		if ( bookEntity.isPresent() ) {
			
			//Mapear entity a dto
			Book_DTO book = modelMapper.map( bookEntity.get(), Book_DTO.class );
			
			return ResponseEntity.ok( book );
		}
		
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	@GetMapping("/gender/{gender}/books")
	private ResponseEntity<?> getByGender(@PathVariable String gender){
		
		//Mapear list entity books a dto
		List<Book_DTO> books = bookRepo.getAllByGender(gender).stream()
				.map(bookEntity -> modelMapper.map(bookEntity, Book_DTO.class))
				.collect(Collectors.toList());
		
		//Si hay libros de este genero..
		if ( ! books.isEmpty() ) {
			
			return ResponseEntity.ok( books );
		}
		
		return ResponseEntity.noContent().build();
	}
	
	
	
	
	@PostMapping( value = "/books", consumes = MediaType.APPLICATION_JSON_VALUE)
	private ResponseEntity<?> create(@RequestBody Book_DTO book) {
		
		//Si el book contiene data..
		if ( book.getName().isBlank() || book.getGender().isBlank() ) {
			
			//Y el genero es admitido
			String gender = book.getGender();
			
			if ( genders.stream().anyMatch( g -> g.equalsIgnoreCase( gender ))) {
					
				//Mapear dto a entity para registrar en bbdd,
				book = modelMapper.map( bookRepo.save( modelMapper.map( book, Book.class )), Book_DTO.class ); //y remapear a dto
				
				return ResponseEntity.ok( book );
			}
		}
		
		return ResponseEntity.badRequest().build();
	}

	
	
	
	
	
	
	
	//---------------INSTNACIAS-------------------
	
	private List<String> genders = Arrays.asList("Terror", "Ciencia-Ficion", "Fantasia"); //Generos admitidos por la Api
	
	@Autowired
	private Book_Repository bookRepo;
	
	@Autowired
	private ModelMapper modelMapper;
}
