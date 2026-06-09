package com.library.api.application.service;

import com.library.api.application.command.BorrowBookCommand;
import com.library.api.application.command.RegisterBookCommand;
import com.library.api.application.command.RegisterBorrowerCommand;
import com.library.api.application.command.ReturnBookCommand;
import com.library.api.application.port.in.BorrowBookUseCase;
import com.library.api.application.port.in.RegisterBookUseCase;
import com.library.api.application.port.in.RegisterBorrowerUseCase;
import com.library.api.application.port.in.ReturnBookUseCase;
import com.library.api.application.response.ApiResponse;
import com.library.api.application.response.BookResponse;
import com.library.api.application.response.BorrowerResponse;
import com.library.api.application.response.BorrowingResponse;
import com.library.api.domain.exception.DomainException;
import com.library.api.domain.exception.ErrorCode;
import com.library.api.domain.valueobject.BookId;
import com.library.api.domain.valueobject.BorrowerId;
import com.library.api.infrastructure.adapter.out.persistence.entity.BookEntity;
import com.library.api.infrastructure.adapter.out.persistence.entity.BorrowerEntity;
import com.library.api.infrastructure.adapter.out.persistence.entity.BorrowingEntity;
import com.library.api.infrastructure.adapter.out.persistence.repository.SpringDataBookRepository;
import com.library.api.infrastructure.adapter.out.persistence.repository.SpringDataBorrowerRepository;
import com.library.api.infrastructure.adapter.out.persistence.repository.SpringDataBorrowingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryFacadeService implements RegisterBorrowerUseCase, RegisterBookUseCase, BorrowBookUseCase, ReturnBookUseCase {

    private final SpringDataBorrowerRepository borrowerRepository;
    private final SpringDataBookRepository bookRepository;
    private final SpringDataBorrowingRepository borrowingRepository;

    /**
     * 👥 Use Case: Register Borrower
     */
    @Override
    @Transactional
    public ApiResponse<BorrowerResponse> execute(RegisterBorrowerCommand command) {
        if (borrowerRepository.existsByEmail(command.email())) {
            throw new DomainException(ErrorCode.DUPLICATE_EMAIL);
        }

        String secureRefNo = BorrowerId.generate().value();
        var entity = new BorrowerEntity(secureRefNo, command.name(), command.email());
        var saved = borrowerRepository.save(entity);

        var data = new BorrowerResponse(saved.getRefNo(), saved.getName(), saved.getEmail());
        return new ApiResponse<>("0000", "SUCCESS", ZonedDateTime.now(), data);
    }

    /**
     * 📘 Use Case: Register Book Copy (from RegisterBookUseCase)
     */
    @Override
    @Transactional
    public ApiResponse<BookResponse> execute(RegisterBookCommand command) {
        var existingIsbnMatch = bookRepository.findFirstByIsbn(command.isbn());
        if (existingIsbnMatch.isPresent()) {
            BookEntity existingBook = existingIsbnMatch.get();
            if (!existingBook.getTitle().equalsIgnoreCase(command.title().trim()) ||
                    !existingBook.getAuthor().equalsIgnoreCase(command.author().trim())) {
                throw new DomainException(ErrorCode.ISBN_CONFLICT);
            }
        }

        String compactId = BookId.generate().value();
        var entity = new BookEntity(compactId, command.isbn().trim(), command.title().trim(), command.author().trim());
        var saved = bookRepository.save(entity);

        // Maps perfectly to your 5-field BookResponse record structure
        var data = new BookResponse(
                saved.getCopyId(),
                saved.getIsbn(),
                saved.getTitle(),
                saved.getAuthor(),
                false // New books are always initialized as available
        );
        return new ApiResponse<>("0000", "SUCCESS", ZonedDateTime.now(), data);
    }

    /**
     * 📚 Use Case: Retrieve All Books (from RegisterBookUseCase)
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<BookResponse>> getAllBooks() {
        log.info("Fetching complete library inventory catalog ledger...");

        List<BookResponse> allBooks = bookRepository.findAll().stream()
                .map(book -> {
                    // Calculate live loan status for this individual item row
                    boolean isCurrentlyBorrowed = borrowingRepository.isBookCopyCurrentlyBorrowed(book.getCopyId());

                    // Maps perfectly to your 5-field BookResponse record structure
                    return new BookResponse(
                            book.getCopyId(),
                            book.getIsbn(),
                            book.getTitle(),
                            book.getAuthor(),
                            isCurrentlyBorrowed
                    );
                })
                .toList();

        return new ApiResponse<>("0000", "SUCCESS", ZonedDateTime.now(), allBooks);
    }

    /**
     * 🛒 Use Case: Borrow Book Copy
     */
    @Override
    @Transactional
    public ApiResponse<BorrowingResponse> execute(BorrowBookCommand command) {
        var borrower = borrowerRepository.findById(command.borrowerId())
                .orElseThrow(() -> new DomainException(ErrorCode.BORROWER_NOT_FOUND));

        var bookCopy = bookRepository.findById(command.isbn())
                .orElseThrow(() -> new DomainException(ErrorCode.BOOK_NOT_FOUND));

        if (borrowingRepository.isBookCopyCurrentlyBorrowed(bookCopy.getCopyId())) {
            throw new DomainException(ErrorCode.INVALID_REQUEST);
        }

        String txnId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        var borrowing = new BorrowingEntity(txnId, borrower, bookCopy, LocalDateTime.now(), null);
        var saved = borrowingRepository.save(borrowing);

        var data = new BorrowingResponse(saved.getId(), saved.getBorrower().getRefNo(), saved.getBook().getCopyId(), saved.getBorrowedAt(), saved.getReturnedAt());
        return new ApiResponse<>("0000", "SUCCESS", ZonedDateTime.now(), data);
    }

    /**
     * 🔄 Use Case: Return Book Copy
     */
    @Override
    @Transactional
    public ApiResponse<BorrowingResponse> execute(ReturnBookCommand command) {
        var activeBorrowing = borrowingRepository.findActiveLoanRecord(command.borrowerId(), command.isbn())
                .orElseThrow(() -> new DomainException(ErrorCode.INVALID_REQUEST));

        activeBorrowing.setReturnedAt(LocalDateTime.now());
        var updated = borrowingRepository.save(activeBorrowing);

        var data = new BorrowingResponse(updated.getId(), updated.getBorrower().getRefNo(), updated.getBook().getCopyId(), updated.getBorrowedAt(), updated.getReturnedAt());
        return new ApiResponse<>("0000", "SUCCESS", ZonedDateTime.now(), data);
    }
}