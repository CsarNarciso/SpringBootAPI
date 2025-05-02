package com.cesar.BookApi.service;

import com.cesar.BookApi.dto.bookDTO;
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

	public List<bookDTO> getAll() {

		//Mapping list entity books to DTO
		return bookRepo.findAll().stream()
				.map(bookEntity -> modelMapper.map(bookEntity, bookDTO.class))
				.toList();
	}


	public bookDTO getById(Long book_id) {

		//Searching book entity by id
		Optional<Book> bookEntity = bookRepo.findById(book_id);

		//If book exist..
		if (bookEntity.isPresent()) {

			//Mapping entity to DTO
			return modelMapper.map(bookEntity.get(), bookDTO.class);
		}
		return null;
	}


	public List<bookDTO> getByGenre(String genre) {

		List<bookDTO> books = Collections.emptyList();

		if (!genre.isEmpty()) {

			//Mapping list entity books to DTO
			books = bookRepo.getByGenre(genre).stream()
					.map(bookEntity -> modelMapper.map(bookEntity, bookDTO.class))
					.toList();
		}
		return books;
	}


	public bookDTO create(bookDTO book) {

		//Mapping DTO to entity to register in DB,
		book = modelMapper.map(bookRepo.save(modelMapper.map(book, Book.class)), bookDTO.class); //and re-mapping result to DTO

		return book;
	}


	public bookDTO replace(Long book_id, bookDTO replaceBook) {

		//Mapping DTO to entity to replace in DB,
		replaceBook = modelMapper.map(bookRepo.save(modelMapper.map(replaceBook, Book.class)), bookDTO.class); //and re-mapping to DTO

		return replaceBook;
	}


	public bookDTO update(Long book_id, Map<String, Object> fields) {

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
			bookDTO dtoValidate = modelMapper.map(book, bookDTO.class);


			//Validate
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<bookDTO>> violations = validator.validate(dtoValidate);

			if (!violations.isEmpty()) {

				throw new ConstraintViolationException(violations);
			}

			//Save change entity to update in DB,
			bookDTO updateBook = modelMapper.map(bookRepo.save(book), bookDTO.class); //and re-mapping to DTO

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