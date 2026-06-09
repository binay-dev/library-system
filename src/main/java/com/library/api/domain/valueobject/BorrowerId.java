package com.library.api.domain.valueobject;

import java.util.UUID;

public record BorrowerId(String value) {
    public static BorrowerId generate() {
        return new BorrowerId("BWR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }
}