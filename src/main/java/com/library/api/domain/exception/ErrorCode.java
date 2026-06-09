package com.library.api.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    DUPLICATE_EMAIL(
            "DUPLICATE_EMAIL",
            "Email already exists. Please use a different email."
    ),

    ISBN_CONFLICT(
            "ISBN_CONFLICT",
            "Book with this ISBN already exists in the system."
    ),

    BORROWER_NOT_FOUND(
            "BORROWER_NOT_FOUND",
            "Borrower not found for the given identifier."
    ),

    BOOK_NOT_FOUND(
            "BOOK_NOT_FOUND",
            "Book not found in the system."
    ),

    INVALID_REQUEST(
            "INVALID_REQUEST",
            "Request data is invalid. Please check input values."
    );

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}