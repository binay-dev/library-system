package com.library.api.application.service;

import com.library.api.application.command.RegisterBookCommand;
import com.library.api.application.mapper.ApplicationResponseMapper;
import com.library.api.application.port.in.RegisterBookUseCase;
import com.library.api.application.port.out.BookRepositoryPort;
import com.library.api.application.port.out.BorrowingRepositoryPort;
import com.library.api.application.response.*;
import com.library.api.domain.model.Book;
import com.library.api.domain.valueobject.BookId;
import com.library.api.domain.valueobject.Isbn;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class BookApplicationService implements RegisterBookUseCase {
    private final BookRepositoryPort bookRepository;
    private final BorrowingRepositoryPort borrowingRepository;

    public BookApplicationService(BookRepositoryPort bookRepository, BorrowingRepositoryPort borrowingRepository) {
        this.bookRepository = bookRepository;
        this.borrowingRepository = borrowingRepository;
    }

    @Override
    public ApiResponse<BookResponse> execute(RegisterBookCommand command) {
        Isbn isbn = new Isbn(command.isbn());

        Book bookToSave = bookRepository.findFirstByIsbn(isbn)
                .map(existing -> new Book(BookId.generate(), isbn, existing.getTitle(), existing.getAuthor()))
                .orElseGet(() -> new Book(BookId.generate(), isbn, command.title(), command.author()));

        Book saved = bookRepository.save(bookToSave);
        return new ApiResponse<>("0000", "SUCCESS",ZonedDateTime.now(),
                ApplicationResponseMapper.toResponse(saved, false));
    }

    @Override
    public ApiResponse<List<BookResponse>> getAllBooks() {
        List<BookResponse> payload = bookRepository.findAll().stream()
                .map(b -> {
                    boolean isBorrowed = borrowingRepository.isBookCurrentlyBorrowed(b.getId());
                    return ApplicationResponseMapper.toResponse(b, isBorrowed);
                })
                .toList();
        return new ApiResponse<>("0000", "SUCCESS",ZonedDateTime.now(), payload);
    }
}