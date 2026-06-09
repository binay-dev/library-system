package com.library.api.application.response;

public record BookResponse(String copyId, String isbn, String title, String author, boolean isBorrowed) {}