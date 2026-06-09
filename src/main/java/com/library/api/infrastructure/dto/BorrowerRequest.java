package com.library.api.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BorrowerRequest(
        @NotBlank(message = "Borrower profile designation name string is required.")
        String name,

        @NotBlank(message = "Functional destination mailbox registration electronic address is required.")
        @Email(message = "Supplied data block fails general formatting validation criteria rules.")
        String email
) {}