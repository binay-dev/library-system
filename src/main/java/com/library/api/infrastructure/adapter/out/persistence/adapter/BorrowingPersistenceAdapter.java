package com.library.api.infrastructure.adapter.out.persistence.adapter;

import com.library.api.application.port.out.BorrowingRepositoryPort;
import com.library.api.domain.model.Borrowing;
import com.library.api.domain.valueobject.BookId;
import com.library.api.domain.valueobject.BorrowerId;
import com.library.api.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.library.api.infrastructure.adapter.out.persistence.repository.SpringDataBorrowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BorrowingPersistenceAdapter implements BorrowingRepositoryPort {
    private final SpringDataBorrowingRepository repository;

    @Override
    public boolean isBookCurrentlyBorrowed(BookId bookId) {
        return repository.isBookCopyCurrentlyBorrowed(bookId.value());
    }

    @Override
    public Optional<Borrowing> findActiveBorrowing(BorrowerId borrowerId, BookId bookId) {
        return repository.findActiveLoanRecord(
                borrowerId.value(),
                bookId.value()
        ).map(EntityMapper::toDomain);
    }

    @Override
    public Borrowing save(Borrowing borrowing) {
        var entity = EntityMapper.toEntity(borrowing);
        var savedEntity = repository.save(entity);
        return EntityMapper.toDomain(savedEntity);
    }
}