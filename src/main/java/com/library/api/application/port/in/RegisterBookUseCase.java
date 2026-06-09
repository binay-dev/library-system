package com.library.api.application.port.in;
import com.library.api.application.command.RegisterBookCommand;
import com.library.api.application.response.*;
import java.util.List;


public interface RegisterBookUseCase {
    ApiResponse<BookResponse> execute(RegisterBookCommand command);
    ApiResponse<List<BookResponse>> getAllBooks();
}