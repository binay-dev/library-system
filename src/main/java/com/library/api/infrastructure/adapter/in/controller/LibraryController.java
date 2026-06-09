package com.library.api.infrastructure.adapter.in.controller;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
@Tag(name = "Library Management Core System", description = "Consolidated endpoints for books, borrowers, and loan workflows")
public class LibraryController {

    private final RegisterBorrowerUseCase registerBorrowerUseCase;
    private final RegisterBookUseCase registerBookUseCase;
    private final BorrowBookUseCase borrowBookUseCase;
    private final ReturnBookUseCase returnBookUseCase;

    @PostMapping("/borrowers")
    @Operation(summary = "Register a new borrower profile", description = "Persists a unique library membership profile based on a unique email address.")
    public ResponseEntity<ApiResponse<BorrowerResponse>> registerBorrower(@RequestBody RegisterBorrowerCommand command) {
        log.info("REST Request received to register borrower with email: {}", command.email());
        var response = registerBorrowerUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/books")
    @Operation(summary = "Register a new book copy item asset", description = "Appends a new physical tracking instance row. Bypasses unique checks if metadata completely matches a known ISBN.")
    public ResponseEntity<ApiResponse<BookResponse>> registerBook(@RequestBody RegisterBookCommand command) {
        log.info("REST Request received to register book copy: '{}' by {}", command.title(), command.author());
        var response = registerBookUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/books")
    @Operation(summary = "Fetch full active inventory ledger snapshot", description = "Returns a comprehensive array of all copies with live borrowed states.")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks() {
        log.info("REST Request received to fetch full book catalog snapshot.");
        var response = registerBookUseCase.getAllBooks();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/books/borrow")
    @Operation(summary = "Execute book checkout assignment log", description = "Creates an active transaction entry linking a borrower asset code to an available copy asset sequence.")
    public ResponseEntity<ApiResponse<BorrowingResponse>> borrowBook(@RequestBody BorrowBookCommand command) {
        log.info("REST Request received: Borrower '{}' checking out book ISBN: '{}'", command.borrowerId(), command.isbn());
        var response = borrowBookUseCase.execute(command);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/books/return")
    @Operation(summary = "Finalize an outstanding borrow tracking row", description = "Validates an active outstanding loan and applies a closing return timestamp marker.")
    public ResponseEntity<ApiResponse<BorrowingResponse>> returnBook(@RequestBody ReturnBookCommand command) {
        log.info("REST Request received: Borrower '{}' returning book ISBN: '{}'", command.borrowerId(), command.isbn());
        var response = returnBookUseCase.execute(command);
        return ResponseEntity.ok(response);
    }
}