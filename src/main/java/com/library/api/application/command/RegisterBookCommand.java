package com.library.api.application.command;
public record RegisterBookCommand(String isbn, String title, String author) {}