package com.library.api.application.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;
@Schema(description = "Generic API Response Wrapper")
public record ApiResponse<T>(
        @Schema(example = "0000", description = "Response status code")
        String code,

        @Schema(example = "SUCCESS", description = "Human-readable status message")
        String message,

        @Schema(description = "ISO Timestamp")
        ZonedDateTime timestamp,

        // 💡 Add this annotation so SpringDoc can dynamically evaluate whatever object maps inside
        @Schema(description = "The payload body returned by the operation")
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                "SUCCESS",
                "Request processed successfully",
                ZonedDateTime.now(),
                data
        );
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<>(
                code,
                message,
                ZonedDateTime.now(),
                null
        );
    }

}