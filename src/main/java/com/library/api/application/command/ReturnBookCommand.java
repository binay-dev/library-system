package com.library.api.application.command;

/**
 * An immutable input command capsule carrying the necessary payload
 * parameters to initiate a book return workflow transaction.
 */
public record ReturnBookCommand(
        String borrowerId,
        String isbn
) {}