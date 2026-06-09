package com.library.api.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        @NotBlank(message = "Asset metadata indexing registration value ISBN is mandatory.") String isbn,
        @NotBlank(message = "Asset literary title context cannot be left blank.") String title,
        @NotBlank(message = "Asset original source content author designation string is required.") String author
) {}