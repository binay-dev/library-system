package com.library.api.constant;

import lombok.Getter;

@Getter
public enum ResponseCode {

   SUCCESS("0000", "Transaction completed successfully."),
   INVALID_PAYLOAD("B-4000", "Invalid request message payload format parameters."),
   DUPLICATE_RECORD("B-4001", "Record matching target identifier already exists in system context."),
   RESOURCE_NOT_FOUND("B-4040", "Requested target resource entity index could not be located."),
   INTERNAL_SYSTEM_ERROR("B-5999", "An unexpected system exception occurred inside our core processing layer.");

    private final String code;
    private final String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}