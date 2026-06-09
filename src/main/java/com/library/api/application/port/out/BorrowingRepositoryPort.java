package com.library.api.application.port.out;

import com.library.api.domain.model.Borrowing;
import com.library.api.domain.valueobject.BookId;
import com.library.api.domain.valueobject.BorrowerId;
import java.util.Optional;

public interface BorrowingRepositoryPort {
    boolean isBookCurrentlyBorrowed(BookId bookId);
    Optional<Borrowing> findActiveBorrowing(BorrowerId borrowerId, BookId bookId);
    Borrowing save(Borrowing borrowing);
}