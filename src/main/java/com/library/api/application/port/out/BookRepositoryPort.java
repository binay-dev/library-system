package com.library.api.application.port.out;

import com.library.api.domain.model.Book;
import com.library.api.domain.valueobject.BookId;
import com.library.api.domain.valueobject.Isbn;
import java.util.List;
import java.util.Optional;

public interface BookRepositoryPort {
    Optional<Book> findById(BookId id);
    Optional<Book> findFirstByIsbn(Isbn isbn);
    List<Book> findAll();
    Book save(Book book);
}