package com.library.api.application.port.in;
import com.library.api.application.command.BorrowBookCommand;
import com.library.api.application.response.ApiResponse;
import com.library.api.application.response.BorrowingResponse;

@FunctionalInterface
public interface BorrowBookUseCase {
    ApiResponse<BorrowingResponse> execute(BorrowBookCommand command);
}