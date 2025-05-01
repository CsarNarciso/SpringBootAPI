package com.cesar.BookApi.service;

import com.cesar.BookApi.dto.Book_DTO;
import com.cesar.BookApi.entity.Book;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {


    private final Service service;

    public BookService(Service service) {
        this.service = service;
    }

    private List<Book_DTO> getAll() {

        //Mapping list entity books to DTO
        return service.bookRepo.findAll().stream()
                .map(bookEntity -> service.modelMapper.map(bookEntity, Book_DTO.class))
                .toList();
    }


    private Book_DTO getById(Long book_id) {

        //Searching book entity by id
        Optional<Book> bookEntity = service.bookRepo.findById(book_id);

        //If book exist..
        if (bookEntity.isPresent()) {

            //Mapping entity to DTO
            return service.modelMapper.map(bookEntity.get(), Book_DTO.class);
        }
        return null;
    }


    private List<Book_DTO> getByGenre(String genre) {

        if (!genre.isEmpty()) {

            //Mapping list entity books to DTO
            List<Book_DTO> books = service.bookRepo.getByGenre(genre).stream()
                    .map(bookEntity -> service.modelMapper.map(bookEntity, Book_DTO.class))
                    .toList();

            return books;
        }
    }


    private Book_DTO create(Book_DTO book) {

        //Set id in null
        book.setId(null);

        //Mapping DTO to entity to register in DB,
        book = service.modelMapper.map(service.bookRepo.save(service.modelMapper.map(book, Book.class)), Book_DTO.class); //and re-mapping result to DTO

        return book;
    }


    private Book_DTO replace(Long book_id, Book_DTO replaceBook) {


        //Set id_book on update data to prevent replacing other book
        replaceBook.setId(book_id);

        //Mapping DTO to entity to replace in BBDD,
        replaceBook = service.modelMapper.map(service.bookRepo.save(service.modelMapper.map(replaceBook, Book.class)), Book_DTO.class); //and re-mapping to DTO

        return replaceBook;
    }
}
