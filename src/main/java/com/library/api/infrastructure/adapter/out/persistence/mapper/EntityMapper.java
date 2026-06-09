package com.library.api.infrastructure.adapter.out.persistence.mapper;

import com.library.api.domain.model.Book;
import com.library.api.domain.model.Borrower;
import com.library.api.domain.model.Borrowing;
import com.library.api.domain.valueobject.BookId;
import com.library.api.domain.valueobject.BorrowerId;
import com.library.api.domain.valueobject.Isbn;
import com.library.api.infrastructure.adapter.out.persistence.entity.BookEntity;
import com.library.api.infrastructure.adapter.out.persistence.entity.BorrowerEntity;
import com.library.api.infrastructure.adapter.out.persistence.entity.BorrowingEntity;

public final class EntityMapper {

    private EntityMapper() {}

    // ==========================================
    // 👥 BORROWER MAPPINGS
    // ==========================================
    public static Borrower toDomain(BorrowerEntity entity) {
        if (entity == null) return null;
        return new Borrower(
                new BorrowerId(entity.getRefNo()),
                entity.getName(),
                entity.getEmail()
        );
    }

    public static BorrowerEntity toEntity(Borrower domain) {
        if (domain == null) return null;
        return new BorrowerEntity(
                domain.getId().value(),
                domain.getName(),
                domain.getEmail()
        );
    }

    // ==========================================
    // 📘 BOOK MAPPINGS
    // ==========================================
    public static Book toDomain(BookEntity entity) {
        if (entity == null) return null;
        return new Book(
                new BookId(entity.getCopyId()), // 🏷️ Correctly tracks specific copy asset primary key
                new Isbn(entity.getIsbn()),     // 📘 Correctly tracks grouping ISBN identifier
                entity.getTitle(),
                entity.getAuthor()
        );
    }

    public static BookEntity toEntity(Book domain) {
        if (domain == null) return null;
        // ✅ Fixed constructor signature (Exactly 4 parameters passed)
        return new BookEntity(
                domain.getId().value(),
                domain.getIsbn().value(),
                domain.getTitle(),
                domain.getAuthor()
        );
    }

    // ==========================================
    // 💳 BORROWING MAPPINGS
    // ==========================================
    public static Borrowing toDomain(BorrowingEntity entity) {
        if (entity == null) return null;

        return Borrowing.builder()
                .id(entity.getId()) // ✅ Fixed: Maps from your true primary key variable `id`
                .borrowerId(new BorrowerId(entity.getBorrower().getRefNo())) // ✅ Fixed traversal path
                .bookId(new BookId(entity.getBook().getCopyId()))           // ✅ Fixed traversal path
                .borrowedAt(entity.getBorrowedAt())
                .returnedAt(entity.getReturnedAt())
                .build();
    }

    public static BorrowingEntity toEntity(Borrowing domain) {
        if (domain == null) return null;

        // 🏗️ Reconstruct required nested entity skeletons with their respective DB keys
        var borrowerSkeleton = new BorrowerEntity();
        borrowerSkeleton.setRefNo(domain.getBorrowerId().value());

        var bookSkeleton = new BookEntity();
        bookSkeleton.setCopyId(domain.getBookId().value());

        // ✅ Fixed: Feeds full Entity instances into the 5-parameter relational constructor
        return new BorrowingEntity(
                domain.getId(),
                borrowerSkeleton,
                bookSkeleton,
                domain.getBorrowedAt(),
                domain.getReturnedAt()
        );
    }
}