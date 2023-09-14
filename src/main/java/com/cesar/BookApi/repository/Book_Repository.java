package com.cesar.BookApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.cesar.BookApi.entity.Book;

@Service
public interface Book_Repository extends JpaRepository<Book, Long>{

	List<Book> getAllByGender(String gender);
}
