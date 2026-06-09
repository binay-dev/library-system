package com.library.api.domain.model;

import com.library.api.domain.valueobject.BookId;
import com.library.api.domain.valueobject.BorrowerId;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class Borrowing {
    private final String id;
    private final BorrowerId borrowerId;
    private final BookId bookId;
    private final LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;

    public void returnBook() {
        if (this.returnedAt != null) {
            throw new IllegalStateException("Domain Rule Violation: Asset already marked as returned.");
        }
        this.returnedAt = LocalDateTime.now();
    }
}