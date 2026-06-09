package com.library.api.application.port.in;
import com.library.api.application.command.ReturnBookCommand;
import com.library.api.application.response.ApiResponse;
import com.library.api.application.response.BorrowingResponse;

public interface ReturnBookUseCase {
    ApiResponse<BorrowingResponse> execute(ReturnBookCommand command);
}