package com.library.api.infrastructure.adapter.out.persistence.repository;

import com.library.api.infrastructure.adapter.out.persistence.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataBookRepository extends JpaRepository<BookEntity, String> {
    Optional<BookEntity> findFirstByIsbn(String isbn);
}