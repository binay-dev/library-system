package com.library.api.infrastructure.adapter.in.controller.mapper;

import com.library.api.application.command.RegisterBookCommand;
import com.library.api.application.command.RegisterBorrowerCommand;
import com.library.api.infrastructure.dto.BookRequest;
import com.library.api.infrastructure.dto.BorrowerRequest;

public class ControllerMapper {

    /**
     * Maps an inbound HTTP Borrower Request DTO into an internal RegisterBorrowerCommand record.
     */
    public static RegisterBorrowerCommand toCommand(BorrowerRequest request) {
        if (request == null) {
            return null;
        }
        return new RegisterBorrowerCommand(
                request.name(),
                request.email()
        );
    }

    /**
     * Maps an inbound HTTP Book Request DTO into an internal RegisterBookCommand record.
     */
    public static RegisterBookCommand toCommand(BookRequest request) {
        if (request == null) {
            return null;
        }
        return new RegisterBookCommand(
                request.isbn(),
                request.title(),
                request.author()
        );
    }
}