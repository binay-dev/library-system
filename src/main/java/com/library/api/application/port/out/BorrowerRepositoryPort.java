package com.library.api.application.port.out;

import com.library.api.domain.model.Borrower;
import com.library.api.domain.valueobject.BorrowerId;
import java.util.Optional;

public interface BorrowerRepositoryPort {
    Optional<Borrower> findById(BorrowerId id);
    boolean existsByEmail(String email);
    Borrower save(Borrower borrower);
}