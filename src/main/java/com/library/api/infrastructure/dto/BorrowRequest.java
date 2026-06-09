package com.library.api.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record BorrowRequest(
        @NotBlank(message = "Transactional profile origin identifier mapping context index is required.")
        String borrowerId,

        @NotBlank(message = "Transactional asset destination identifier indexing tracker key token is required.")
        String bookId
) {}