package com.library.api.infrastructure.adapter.out.persistence.adapter;

import com.library.api.application.port.out.BookRepositoryPort;
import com.library.api.domain.model.Book;
import com.library.api.domain.valueobject.BookId;
import com.library.api.domain.valueobject.Isbn;
import com.library.api.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.library.api.infrastructure.adapter.out.persistence.repository.SpringDataBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookPersistenceAdapter implements BookRepositoryPort {
    private final SpringDataBookRepository repository;

    @Override
    public Optional<Book> findById(BookId id) {
        return repository.findById(id.value()).map(EntityMapper::toDomain);
    }

    @Override
    public Optional<Book> findFirstByIsbn(Isbn isbn) {
        return repository.findById(isbn.value()).map(EntityMapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll().stream().map(EntityMapper::toDomain).toList();
    }

    @Override
    public Book save(Book book) {
        var entity = EntityMapper.toEntity(book);
        var savedEntity = repository.save(entity);
        return EntityMapper.toDomain(savedEntity);
    }
}