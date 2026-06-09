package com.library.api.infrastructure.adapter.out.persistence.repository;

import com.library.api.infrastructure.adapter.out.persistence.entity.BorrowingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SpringDataBorrowingRepository extends JpaRepository<BorrowingEntity, String> {
    @Query("SELECT COUNT(b) > 0 FROM BorrowingEntity b WHERE b.book.copyId = :copyId AND b.returnedAt IS NULL")
    boolean isBookCopyCurrentlyBorrowed(@Param("copyId") String copyId);
    @Query("SELECT b FROM BorrowingEntity b WHERE b.borrower.refNo = :borrowerRefNo AND b.book.copyId = :copyId AND b.returnedAt IS NULL")
    Optional<BorrowingEntity> findActiveLoanRecord(@Param("borrowerRefNo") String borrowerRefNo, @Param("copyId") String copyId);}