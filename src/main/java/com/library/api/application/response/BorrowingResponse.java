package com.library.api.application.response;

import java.time.LocalDateTime;

public record BorrowingResponse(String transactionId, String borrowerRefNo, String copyId, LocalDateTime borrowedAt, LocalDateTime returnedAt) {}