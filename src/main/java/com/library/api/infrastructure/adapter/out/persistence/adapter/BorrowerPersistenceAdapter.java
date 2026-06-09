package com.library.api.infrastructure.adapter.out.persistence.adapter;

import com.library.api.application.port.out.BorrowerRepositoryPort;
import com.library.api.domain.model.Borrower;
import com.library.api.domain.valueobject.BorrowerId;
import com.library.api.infrastructure.adapter.out.persistence.mapper.EntityMapper;
import com.library.api.infrastructure.adapter.out.persistence.repository.SpringDataBorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BorrowerPersistenceAdapter implements BorrowerRepositoryPort {
    private final SpringDataBorrowerRepository repository;

    @Override
    public Optional<Borrower> findById(BorrowerId id) {
        return repository.findById(id.value()).map(EntityMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Borrower save(Borrower borrower) {
        var entity = EntityMapper.toEntity(borrower);
        var savedEntity = repository.save(entity);
        return EntityMapper.toDomain(savedEntity);
    }
}