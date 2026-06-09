package com.library.api.application.service;

import com.library.api.application.command.BorrowBookCommand;
import com.library.api.application.command.RegisterBookCommand;
import com.library.api.application.command.RegisterBorrowerCommand;
import com.library.api.application.command.ReturnBookCommand;
import com.library.api.application.response.ApiResponse;
import com.library.api.application.response.BookResponse;
import com.library.api.application.response.BorrowerResponse;
import com.library.api.application.response.BorrowingResponse;
import com.library.api.domain.exception.DomainException;
import com.library.api.domain.exception.ErrorCode;
import com.library.api.infrastructure.adapter.out.persistence.entity.BookEntity;
import com.library.api.infrastructure.adapter.out.persistence.entity.BorrowerEntity;
import com.library.api.infrastructure.adapter.out.persistence.entity.BorrowingEntity;
import com.library.api.infrastructure.adapter.out.persistence.repository.SpringDataBookRepository;
import com.library.api.infrastructure.adapter.out.persistence.repository.SpringDataBorrowerRepository;
import com.library.api.infrastructure.adapter.out.persistence.repository.SpringDataBorrowingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryFacadeServiceTest {

    @Mock
    private SpringDataBorrowerRepository borrowerRepository;

    @Mock
    private SpringDataBookRepository bookRepository;

    @Mock
    private SpringDataBorrowingRepository borrowingRepository;

    @InjectMocks
    private LibraryFacadeService libraryFacadeService;

    // =========================================================================
    // 👥 BORROWER REGISTRATION TESTS
    // =========================================================================
    @Nested
    @DisplayName("Use Case: Register Borrower")
    class RegisterBorrowerTests {

        @Test
        @DisplayName("Should successfully register a new borrower when email is unique")
        void registerBorrowerSuccess() {
            RegisterBorrowerCommand command = new RegisterBorrowerCommand("John Doe", "john@example.com");
            BorrowerEntity savedEntity = new BorrowerEntity("BWR-12345", "John Doe", "john@example.com");

            when(borrowerRepository.existsByEmail(command.email())).thenReturn(false);
            when(borrowerRepository.save(any(BorrowerEntity.class))).thenReturn(savedEntity);

            ApiResponse<BorrowerResponse> response = libraryFacadeService.execute(command);

            assertNotNull(response);
            assertEquals("0000", response.code());
            assertEquals("john@example.com", response.data().email());
            verify(borrowerRepository, times(1)).save(any(BorrowerEntity.class));
        }

        @Test
        @DisplayName("Should throw DUPLICATE_EMAIL when borrower email already exists")
        void registerBorrowerDuplicateEmail() {
            RegisterBorrowerCommand command = new RegisterBorrowerCommand("John Doe", "john@example.com");

            when(borrowerRepository.existsByEmail(command.email())).thenReturn(true);

            DomainException exception = assertThrows(DomainException.class, () -> libraryFacadeService.execute(command));
            assertEquals(ErrorCode.DUPLICATE_EMAIL, exception.getErrorCode());
            verify(borrowerRepository, never()).save(any(BorrowerEntity.class));
        }
    }

    // =========================================================================
    // 📘 BOOK REGISTRATION & CATALOG TESTS
    // =========================================================================
    @Nested
    @DisplayName("Use Case: Register & View Books")
    class BookTests {

        @Test
        @DisplayName("Scenario 1 & 3: Allow duplicate entries with identical details or distinct ISBNs")
        void registerBookSuccess() {
            RegisterBookCommand command = new RegisterBookCommand("9780134494166", "Clean Architecture", "Robert C. Martin");
            BookEntity existingMatch = new BookEntity("BK-001", "9780134494166", "Clean Architecture", "Robert C. Martin");

            when(bookRepository.findFirstByIsbn(command.isbn())).thenReturn(Optional.of(existingMatch));
            when(bookRepository.save(any(BookEntity.class))).thenAnswer(inv -> inv.getArgument(0));

            ApiResponse<BookResponse> response = libraryFacadeService.execute(command);

            assertEquals("0000", response.code());
            assertEquals("Clean Architecture", response.data().title());
            verify(bookRepository, times(1)).save(any(BookEntity.class));
        }

        @Test
        @DisplayName("Scenario 2: Reject registration if ISBN matches but metadata differs")
        void registerBookIsbnConflict() {
            RegisterBookCommand command = new RegisterBookCommand("9780134494166", "Mismatched Title", "Wrong Author");
            BookEntity existingMatch = new BookEntity("BK-001", "9780134494166", "Clean Architecture", "Robert C. Martin");

            when(bookRepository.findFirstByIsbn(command.isbn())).thenReturn(Optional.of(existingMatch));

            DomainException exception = assertThrows(DomainException.class, () -> libraryFacadeService.execute(command));
            assertEquals(ErrorCode.ISBN_CONFLICT, exception.getErrorCode());
            verify(bookRepository, never()).save(any(BookEntity.class));
        }

        @Test
        @DisplayName("Should retrieve full catalog and correctly map real-time availability fields")
        void getAllBooksSuccess() {
            BookEntity book = new BookEntity("BK-UUID", "9780134494166", "Clean Architecture", "Robert C. Martin");

            when(bookRepository.findAll()).thenReturn(List.of(book));
            when(borrowingRepository.isBookCopyCurrentlyBorrowed("BK-UUID")).thenReturn(true);

            ApiResponse<List<BookResponse>> response = libraryFacadeService.getAllBooks();

            assertNotNull(response);
            assertEquals(1, response.data().size());
            assertTrue(response.data().get(0).isBorrowed());
        }
    }

    // =========================================================================
    // 🛒 BORROW & RETURN WORKFLOW TESTS
    // =========================================================================
    @Nested
    @DisplayName("Use Case: Transactions (Borrow / Return)")
    class TransactionTests {

        @Test
        @DisplayName("Should successfully register loan transaction when copy is available")
        void borrowBookSuccess() {
            BorrowBookCommand command = new BorrowBookCommand("BWR-1", "9780134494166");
            BorrowerEntity borrower = new BorrowerEntity("BWR-1", "Jane", "jane@example.com");
            BookEntity book = new BookEntity("BK-1", "9780134494166", "Clean Architecture", "Robert C. Martin");
            BorrowingEntity savedTxn = new BorrowingEntity("TXN-123", borrower, book, LocalDateTime.now(), null);

            when(borrowerRepository.findById("BWR-1")).thenReturn(Optional.of(borrower));
            when(bookRepository.findById("9780134494166")).thenReturn(Optional.of(book));
            when(borrowingRepository.isBookCopyCurrentlyBorrowed("BK-1")).thenReturn(false);
            when(borrowingRepository.save(any(BorrowingEntity.class))).thenReturn(savedTxn);

            ApiResponse<BorrowingResponse> response = libraryFacadeService.execute(command);

            assertEquals("0000", response.code());
            assertNull(response.data().returnedAt());
            verify(borrowingRepository, times(1)).save(any(BorrowingEntity.class));
        }

        @Test
        @DisplayName("Should throw INVALID_REQUEST if book copy is already out on loan")
        void borrowBookAlreadyBorrowed() {
            BorrowBookCommand command = new BorrowBookCommand("BWR-1", "9780134494166");
            BorrowerEntity borrower = new BorrowerEntity("BWR-1", "Jane", "jane@example.com");
            BookEntity book = new BookEntity("BK-1", "9780134494166", "Clean Architecture", "Robert C. Martin");

            when(borrowerRepository.findById("BWR-1")).thenReturn(Optional.of(borrower));
            when(bookRepository.findById("9780134494166")).thenReturn(Optional.of(book));
            when(borrowingRepository.isBookCopyCurrentlyBorrowed("BK-1")).thenReturn(true);

            DomainException exception = assertThrows(DomainException.class, () -> libraryFacadeService.execute(command));
            assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
        }

        @Test
        @DisplayName("Should finalize log record with a return timestamp upon processing return commands")
        void returnBookSuccess() {
            ReturnBookCommand command = new ReturnBookCommand("BWR-1", "BK-1");
            BorrowerEntity borrower = new BorrowerEntity("BWR-1", "Jane", "jane@example.com");
            BookEntity book = new BookEntity("BK-1", "9780134494166", "Clean Architecture", "Robert C. Martin");
            BorrowingEntity activeLoan = new BorrowingEntity("TXN-123", borrower, book, LocalDateTime.now().minusDays(2), null);

            when(borrowingRepository.findActiveLoanRecord("BWR-1", "BK-1")).thenReturn(Optional.of(activeLoan));
            when(borrowingRepository.save(any(BorrowingEntity.class))).thenAnswer(inv -> inv.getArgument(0));

            ApiResponse<BorrowingResponse> response = libraryFacadeService.execute(command);

            assertEquals("0000", response.code());
            assertNotNull(response.data().returnedAt());
        }
    }
}