package com.library.api.application.port.in;

import com.library.api.application.command.RegisterBorrowerCommand;
import com.library.api.application.response.ApiResponse;
import com.library.api.application.response.BorrowerResponse;

@FunctionalInterface
public interface RegisterBorrowerUseCase {
    ApiResponse<BorrowerResponse> execute(RegisterBorrowerCommand command);
}