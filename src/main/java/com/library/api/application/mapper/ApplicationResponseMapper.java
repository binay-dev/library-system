package com.library.api.application.mapper;

import com.library.api.application.response.BookResponse;
import com.library.api.application.response.BorrowerResponse;
import com.library.api.domain.model.Book;
import com.library.api.domain.model.Borrower;

public class ApplicationResponseMapper {

    public static BorrowerResponse toResponse(Borrower borrower) {
        return new BorrowerResponse(
                borrower.getId().value().toString(),
                borrower.getName(),
                borrower.getEmail()
        );
    }

    public static BookResponse toResponse(Book book, boolean isBorrowed) {
        return new BookResponse(
                book.getId().value().toString(),
                book.getIsbn().value(),
                book.getTitle(),
                book.getAuthor(),
                isBorrowed
        );
    }
}