package com.cesar.BookApi.service;

import com.cesar.BookApi.dto.Book_DTO;
import com.cesar.BookApi.entity.Book;
import com.cesar.BookApi.repository.Book_Repository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class BookService {

	public List<Book_DTO> getAll() {

		//Mapping list entity books to DTO
		return bookRepo.findAll().stream()
				.map(bookEntity -> modelMapper.map(bookEntity, Book_DTO.class))
				.toList();
	}


	public Book_DTO getById(Long book_id) {

		//Searching book entity by id
		Optional<Book> bookEntity = bookRepo.findById(book_id);

		//If book exist..
		if (bookEntity.isPresent()) {

			//Mapping entity to DTO
			return modelMapper.map(bookEntity.get(), Book_DTO.class);
		}
		return null;
	}


	public List<Book_DTO> getByGenre(String genre) {

		List<Book_DTO> books = Collections.emptyList();

		if (!genre.isEmpty()) {

			//Mapping list entity books to DTO
			books = bookRepo.getByGenre(genre).stream()
					.map(bookEntity -> modelMapper.map(bookEntity, Book_DTO.class))
					.toList();
		}
		return books;
	}


	public Book_DTO create(Book_DTO book) {

		//Set id in null
		book.setId(null);

		//Mapping DTO to entity to register in DB,
		book = modelMapper.map(bookRepo.save(modelMapper.map(book, Book.class)), Book_DTO.class); //and re-mapping result to DTO

		return book;
	}


	public Book_DTO replace(Long book_id, Book_DTO replaceBook) {


		//Set id_book on update data to prevent replacing other book
		replaceBook.setId(book_id);

		//Mapping DTO to entity to replace in DB,
		replaceBook = modelMapper.map(bookRepo.save(modelMapper.map(replaceBook, Book.class)), Book_DTO.class); //and re-mapping to DTO

		return replaceBook;
	}


	public Book_DTO update(Long book_id, Map<String, Object> fields) {

		Optional<Book> optionalBook = bookRepo.findById(book_id);

		//if this book already exists..
		if (optionalBook.isPresent()) {

			Book book = optionalBook.get();

			//Remove id to prevent change in entity.
			fields.remove("id");


			//Set change fields to Entity
			fields.forEach((k, v) -> {

				Field field = ReflectionUtils.findField(Book.class, k);

				if (field != null) {

					field.setAccessible(true);
					ReflectionUtils.setField(field, book, v);
				}
			});

			//Mapping to DTO to validate
			Book_DTO dtoValidate = modelMapper.map(book, Book_DTO.class);


			//Validate
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<Book_DTO>> violations = validator.validate(dtoValidate);

			if (!violations.isEmpty()) {

				throw new ConstraintViolationException(violations);
			}

			//Save change entity to update in DB,
			Book_DTO updateBook = modelMapper.map(bookRepo.save(book), Book_DTO.class); //and re-mapping to DTO

			return updateBook;
		}
		return null;
	}


	public void delete(Long book_id) {

		Optional<Book> optionalBook = bookRepo.findById(book_id);

		//if this book exists..
		if (optionalBook.isPresent()) {

			//Delete from DB
			bookRepo.deleteById(book_id);
		}
	}


	@Autowired
	private Book_Repository bookRepo;

	@Autowired
	private ModelMapper modelMapper;
}