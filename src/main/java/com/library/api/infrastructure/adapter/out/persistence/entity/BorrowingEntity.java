package com.library.api.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrowings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingEntity {

    @Id
    @Column(name = "ref_no", length = 50, nullable = false, updatable = false)
    private String id; // Matches the physical `ref_no` primary key column from MySQL DESCRIBE

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "borrower_ref_no", referencedColumnName = "ref_no", nullable = false)
    private BorrowerEntity borrower;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // ✅ Explicitly set column to book_isbn, referencing the target key column copy_id
    @JoinColumn(name = "book_isbn", referencedColumnName = "copy_id", nullable = false)
    private BookEntity book;

    @Column(name = "borrowed_at", nullable = false, updatable = false)
    private LocalDateTime borrowedAt;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;
}