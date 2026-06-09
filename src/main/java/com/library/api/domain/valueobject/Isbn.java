package com.library.api.domain.valueobject;

public record Isbn(String value) {
    public Isbn {
        if (value == null || value.length() != 13) {
            throw new IllegalArgumentException("Domain Rule Violation: ISBN must be exactly 13 characters.");
        }
    }
}