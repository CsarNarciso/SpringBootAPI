package com.cesar.SpringBootAPI.service;

import com.cesar.SpringBootAPI.dto.BookDTO;
import com.cesar.SpringBootAPI.entity.Book;
import com.cesar.SpringBootAPI.exception.NoContentException;
import com.cesar.SpringBootAPI.exception.NotFoundException;
import com.cesar.SpringBootAPI.repository.BookRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class BookService {


	public List<BookDTO> getAll() {

		List<Book> books = repo.findAll();

		if(books.isEmpty()){
			throw  new NoContentException();
		}

		//Map entities to DTOs
		return books.stream()
				.map(entity -> mapper.map(entity, BookDTO.class))
				.toList(); //and return as list
	}


	public BookDTO getById(Long id) {

		Optional<Book> book = repo.findById(id);

		if(book.isEmpty()){
			throw new NotFoundException();
		}

		//Map entity as DTO response
		return mapper.map(book.get(), BookDTO.class);
	}


	public List<BookDTO> getByGenre(String genre) {

		List<Book> books = repo.getByGenre(genre);

		if(books.isEmpty()){
			throw  new NoContentException();
		}

		//Map entities to DTOs
		return books.stream()
				.map(entity -> mapper.map(entity, BookDTO.class))
				.toList(); //and return as list
	}


	public BookDTO create(BookDTO createRequest) {

		//Map create request DTO fields to entity object
		Book entity = mapper.map(createRequest, Book.class);

		//, to save in DB,
		entity = repo.save(entity);

		//Re-map saved entity to DTO as response
		return mapper.map(entity, BookDTO.class);
	}


	public BookDTO replace(Long id, BookDTO newBookRequest) {

		if(repo.findById(id).isPresent()) {

			//Map DTO request fields to entity
			Book newBook = mapper.map(newBookRequest, Book.class);

			//Update existent entity in DB
			newBook.setId(id);
			newBook = repo.save(newBook);

			//And map back updated entity as DTO response
			return mapper.map(newBook, BookDTO.class);
		}
		throw  new NotFoundException();
	}


	public BookDTO update(Long id, Map<String, Object> fields) {

		Optional<Book> existentBook = repo.findById(id);

		if (existentBook.isEmpty()) {
			throw new NotFoundException();
		}

		Book updatedBook = existentBook.get();

		//Remove ID from update request fields to prevent this to be updated in entity.
		fields.remove("id");

		//Inject update request fields on entity
		for( Map.Entry<String, Object> entry : fields.entrySet() ) {

			Field field = ReflectionUtils.findField(Book.class, entry.getKey());

			if (field != null) {

				field.setAccessible(true);
				ReflectionUtils.setField(field, updatedBook, entry.getValue());
			}
		}

		//Map to DTO to validate
		BookDTO dtoValidate = mapper.map(updatedBook, BookDTO.class);

		//Validate
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<BookDTO>> violations = validator.validate(dtoValidate);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}

		//Save updated entity in DB,
		updatedBook = repo.save(updatedBook);

		//and map back as DTO response
		return mapper.map(updatedBook, BookDTO.class);
	}


	public void delete(Long id) {

		if (repo.findById(id).isPresent()) {

			repo.deleteById(id);
			throw new NoContentException();
		}
		throw new NotFoundException();
	}



	public BookService(BookRepository repo, ModelMapper mapper){
		this.repo = repo;
		this.mapper = mapper;
	}

	private final BookRepository repo;
	private final ModelMapper mapper;
}