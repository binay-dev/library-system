package com.library.api.application.command;

/**
 * An immutable input command capsule carrying the necessary payload
 * parameters to initiate a book borrowing workflow transaction.
 */
public record BorrowBookCommand(
        String borrowerId,
        String isbn
) {}