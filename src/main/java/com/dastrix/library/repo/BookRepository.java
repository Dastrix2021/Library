package com.dastrix.library.repo;

import com.dastrix.library.models.Book;
import com.dastrix.library.models.User;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

}
