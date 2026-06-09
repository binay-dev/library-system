package com.library.api.domain.valueobject;

import java.util.UUID;

public record BookId(String value) {
    public static BookId generate() {
        String timeHex = Long.toHexString(System.currentTimeMillis()).toUpperCase();
        String salt = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String raw = "BK-" + timeHex + salt;
        return new BookId(raw.substring(0, 20)); // Guarantees a max length of 20
    }
}